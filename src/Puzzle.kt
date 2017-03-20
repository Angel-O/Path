import java.io.File
import javax.swing.JFileChooser
import java.util.Stack

// Using JFileChooser and nullable type
/* entry point */
fun main(args: Array<String>) {
    val file = chooseFile()
    println(file ?: "No file selected")

	// defensive programming
	if(file == null){
		return
	}

	// creates a matrix containing each location
	val puzzle = createMatrix(file) //File("/Users/specs/git/Path/src/puzzle.txt")
	// create the stack that will contain the path that silves the puzzle
	// and add the starting point (first top left corner of the puzzle)
	val path = Stack<Location>()
	path.push(puzzle[0][0])
	// will collect dead ends that need not to be explored
	val deadEnds = ArrayList<Location>()
	// solve it
	solvePuzzle(puzzle, path, deadEnds)
}

/* opens a pop up window to load the file containing the puzzle */
fun chooseFile(): File? {
    val chooser = JFileChooser()
    val theFileResult = chooser.showOpenDialog(null)
    chooser.setVisible(true)
    when (theFileResult) {
        JFileChooser.APPROVE_OPTION -> return chooser.getSelectedFile()
        JFileChooser.CANCEL_OPTION -> println("Done choosing files.")
        else -> println("Error: Unable to open file.")
    }
    return null
}

/* creates the matrix representing the puzzle to be solved */
fun createMatrix(file: File): ArrayList<List<Location>>{
	val matrix = ArrayList<List<Location>>()
	// initial coordinates
	var row: Int = 0; var col: Int = 0
	// split each line parsing each chunk to an int and create a Location data class
	for (line in file.readLines()){
		matrix.add(line.split(" ").map{ Location(row, col++, it.toInt()) })
		// at each iteration increment the row and reset the column count
		row++; col = 0
	}
	return matrix
}

/* solves the puzzle */
fun solvePuzzle(puzzle: ArrayList<List<Location>>, path: Stack<Location>, fullyExplored: ArrayList<Location>){
	when{
		// if we backtracked all the way back to the initial state, quit
		path.isEmpty() -> { println("The puzzle cannot be solved.") }
		// if the puzzle is solved display the solution
		solved(puzzle, path) -> { println("The solution to the puzzle is:\n" + path.map( {it.coord} )) }
		// otherwise keep exploring
		else -> exploreAll(puzzle, path, fullyExplored)
	}
}

/* explores all possible destinations from the current location */
fun exploreAll(puzzle: ArrayList<List<Location>>, path: Stack<Location>, fullyExplored: ArrayList<Location>){
	// get all possible destinations from the current cell (the one on top of the path stack)
	val destinations = getDestinations(puzzle, path.peek(), fullyExplored)
	// if there is nowhere to go from the current location, backtrack...
	if(destinations.isEmpty()){
		// note: this needs to be "return"ed as it will prevent the below branches
		// from being executed
		return backtrack(puzzle, path, fullyExplored)
	}
	// explore each destination
	while(!destinations.isEmpty()){
		val nextRoute = destinations.pop()
		// if the location hasn't been explored yet, add it to the path and proceed exploring
		if (!path.toList().contains(nextRoute)){
			// note: as above this needs to "return"ed
			path.push(nextRoute)
			return solvePuzzle(puzzle, path, fullyExplored)
		}
	}
	// if none of the destinations turned out to be successful backtrack...
	if(!path.isEmpty()){
		// note: this doesn't need to "return"ed as it is the last branch
		// it will recursively call the solvePuzzle method and keepp the search going
		backtrack(puzzle, path, fullyExplored)
	}
}

/* marks the current location as fully explored (dead end) and goes back to the previous one to try alternative routes */
fun backtrack(puzzle: ArrayList<List<Location>>, path: Stack<Location>, fullyExplored: ArrayList<Location>){
	// mark the current location (top of the stack) as fully explored since we can't go anywhere else
	val explored = path.pop()
	fullyExplored.add(explored)
	solvePuzzle(puzzle, path, fullyExplored)
}

/* checks whether or not the bottom right corner of the puzzle has been reached and therefore the puzzle solved */
fun solved(matrix: ArrayList<List<Location>>, stack: Stack<Location>): Boolean{
	return !stack.isEmpty() && stack.peek().coord == Pair(matrix.size - 1, matrix.size - 1)
}

/* gets a stack of all possible destinations from the current cell based on the range of movement from
  the cell itself */
fun getDestinations(matrix: ArrayList<List<Location>>, currentLocation: Location, fullyExplored: ArrayList<Location>): Stack<Location>{
	val destinations = Stack<Location>()
	val row = currentLocation.coord.first
	val col = currentLocation.coord.second
	val range = currentLocation.range;
	destinations.addAll(listOf(
			createLocation(row, col + range, matrix), // right
			createLocation(row, col - range, matrix), // left
			createLocation(row - range, col, matrix), // top
			createLocation(row + range, col, matrix)) // bottom
			.filter({it != null // ignore null locations (the ones that fall out of the puzzle's boudaries)
					&& (it.range != 0) // ignore destinations that would get us stuck
					&& it.range < matrix.size // ignore locations that would certainly lead to invalid locations outside the matrix
					&& !fullyExplored.contains(it)})) // ignore already visited locations leading to nowhere

	return destinations
}

/* returns a valid coordinate if in range otherwise a null value */
fun createLocation(row: Int, col: Int, matrix: ArrayList<List<Location>>): Location?{
	val puzzleSize = matrix.size
	var location: Location?
	when{
		// create a location data class if the coordinates are in range
		inRange(row, puzzleSize)
		&& inRange(col, puzzleSize) -> {
			location = Location(
					row, // valid row coordinate
					col, // valid column coordinate
					// if it's the goal cell create a location data class with a valid range so that it won't be seen as an invalid location
					if (row == puzzleSize - 1 && col == puzzleSize -1) 1 else matrix[row][col].range)
		}
		else -> location = null
	}
	return location
}

/* checks if the coordinate is withing the puzzle borders */
fun inRange(coord: Int, size: Int): Boolean{
	return coord >= 0 && coord < size
}

