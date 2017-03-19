import java.io.File
import javax.swing.JFileChooser
import java.util.Stack

// Using JFileChooser and nullable type

fun main(args: Array<String>) {
//    val file = chooseFile()
//    println(file ?: "No file selected")
//
//	// defensive programming
//	if(file == null){
//		return
//	}
	// solve the puzzle
    //val matrix = createMatrix(file)
	val puzzle = createMatrix(File("/Users/specs/git/Path/src/puzzle.txt"))
	// debug
	//puzzle.forEach({ println(it) })

	val stack = Stack<Location>()
	stack.push(puzzle[0][0])
	//println(stack[0].coord)
	solvePuzzle(puzzle, stack, ArrayList<Location>())

	//println(stack.contains(Location(0,0,7)))

	//println("FIRST " + puzzle[0][0])
}

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

fun createMatrix(file: File) : ArrayList<List<Location>>{
	val matrix = ArrayList<List<Location>>()

	// initial coordinates
	var row: Int = 0; var col: Int = 0

	for (line in file.readLines()){

		// split each line parsing each chunk to an int
		// and create a Location object adding the coordinates of the cell
		matrix.add(line.split(" ").map{ Location(row, col++, it.toInt()) })

		// at each iteration increment the row and reset the column count
		row++; col = 0
	}
	return matrix
}

fun solvePuzzle(puzzle: ArrayList<List<Location>>, path: Stack<Location>, fullyExplored: ArrayList<Location>): Boolean{

	when{
		// done
		solved(puzzle, path) -> { return true }
		// if we backtracked all the way back to the initial state, quit
		//path.isEmpty() -> { println("there is not a solution"); fullyExplored.forEach({print(it.coord)}); return false }
		path.isEmpty() -> {

			// restart...
			path.push(Location(0, 0, puzzle[0][0].range))
			return solvePuzzle(puzzle, path , fullyExplored)
		}

		else -> {
				// get all possible destinations from the current cell (on top of the path stack)
				val destinations = getDestinations(puzzle, path.peek(), fullyExplored) //destinations.forEach({println(it.range)})
				when{
					// if there is nowhere to go from the current location, backtrack...
					destinations.isEmpty() -> {
						fullyExplored.add(path.pop()); // mark the current location as fully explored
						if (solvePuzzle(puzzle, path, fullyExplored)){
							path.forEach({println(it.coord)})
							return true
						}
					}
					// otherwise keep exploring
					else -> {

						while(!destinations.isEmpty()){ //&& !fullyExplored.contains(destinations.peek())

							val nextRoute = destinations.pop()
							if (nextRoute.coord == Pair(puzzle.size -1, puzzle.size -1))println("CHECKING: " + nextRoute)
							if (!path.toList().contains(nextRoute)){
								// if the location hasn't been explored yet, explore it
								path.push(nextRoute)
								println(path.map{it.coord})
								if (solvePuzzle(puzzle, path, fullyExplored)){
									path.forEach({println(it.coord)})
									return true
								}
							}
						}
						// if none of the destinations turned out to be succesfull backtrack...
						if(!path.isEmpty()){
							val explored = path.pop()
							fullyExplored.add(explored)
							if(solvePuzzle(puzzle, path, fullyExplored)){
								path.forEach({println(it.coord)})
								return true
							}
						}
					}
				}
			}
		}
	return false;
}

fun solved(matrix: ArrayList<List<Location>>, stack: Stack<Location>): Boolean{
	return !stack.isEmpty() && stack.peek().coord == Pair(matrix.size - 1, matrix.size - 1)
}

/* get a stack of all possible destinations from the current cell based on the range of movement from
  the cell itself */
fun getDestinations(matrix: ArrayList<List<Location>>, currentLocation: Location, fullyExplored: ArrayList<Location>): Stack<Location>{
	val destinations = Stack<Location>()
	val row = currentLocation.coord.first
	val col = currentLocation.coord.second
	val range = currentLocation.range;
	val goalCoord = matrix.size - 1; // minus 1 ????
	destinations.addAll(listOf(
			createLocation(row, col + range, matrix), // right
			createLocation(row, col - range, matrix), // left
			createLocation(row - range, col, matrix), // top
			createLocation(row + range, col, matrix)) // bottom
			.filter({it != null // ignore out of range locations
					//&& (it.range != 0 && it.coord != Pair(goalCoord, goalCoord)) // get rid of invalid destinations
					&& it.range < matrix.size // strictly?????
					&& !fullyExplored.contains(it)})) // ignore already visited location that lead to nowhere

	if(destinations.toList().filter({it == Location(goalCoord, goalCoord, matrix[goalCoord][goalCoord].range)}).count() > 0){
		println("END END")
	}

	return destinations
}

/* return a valid coordinate if in range otherwise a null value */
fun createLocation(row: Int, col: Int, matrix: ArrayList<List<Location>>): Location?{
	val puzzleSize = matrix.size
	var location: Location?
	when{
		inRange(row, puzzleSize)
		&& inRange(col, puzzleSize) -> { location = Location(row, col, matrix[row][col].range) }
		else -> location = null
	}
	return location
}

/* check if the coordinate is in range */
fun inRange(coord: Int, size: Int): Boolean{
	return coord >= 0 && coord < size
}

