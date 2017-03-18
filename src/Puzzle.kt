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
	println(stack[0].coord)
	solve(puzzle, stack)

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

fun solve(matrix: ArrayList<List<Location>>, path: Stack<Location>){

	// get the range
	val move = path.peek().range

	// get the possible destinations reacheable from the location on top of the stack
	val destinations = getDestinations(matrix, path.peek())

	destinations.forEach({println(it.range)})
}

/* get a stack of all possible destinations from the current cell based on the range of movement from
  the cell itself */
fun getDestinations(matrix: ArrayList<List<Location>>, currentLocation: Location): Stack<Location>{

	val destinations = Stack<Location>()

	val row = currentLocation.coord.first
	val col = currentLocation.coord.second
	val range = currentLocation.range;

	val right = getLocationIfInRange(row, col + range, matrix)
	val left = getLocationIfInRange(row, col - range, matrix)
	val top = getLocationIfInRange(row - range, col, matrix)
	val bottom = getLocationIfInRange(row + range, col, matrix)

	// ignore null entries
	destinations.addAll(listOf(right, left, top , bottom).filter({it != null}))

	return destinations
}

/* return a valid coordinate if in range otherwise a null value */
fun getLocationIfInRange(row: Int, col: Int, matrix: ArrayList<List<Location>>): Location?{

	val puzzleSize = matrix.count()
	var location: Location?
	when{
		inRange(row, puzzleSize) && inRange(col, puzzleSize) -> location = Location(row, col, matrix[row][col].range)
		else -> location = null
	}
	return location
}

/* check if the coordinate is in range */
fun inRange(coord: Int, size: Int): Boolean{

	return coord >= 0 && coord < size
}

