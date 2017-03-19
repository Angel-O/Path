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
	explore(puzzle, stack)

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

fun explore(matrix: ArrayList<List<Location>>, path: Stack<Location>){

	//if (path.isEmpty()) {path.push(matrix[0][0]); println("path was empty");} // backtracked too much...let's restart : SHOULD NOT HAPPEN
	when{
		solved(matrix, path) -> { path.forEach({println(it.coord)}); return} // print the path...
		else -> {

				// get all possible destinations from the current cell (on top of the path stack)
				val destinations = getDestinations(matrix, path.peek())

				//destinations.forEach({println(it.range)})
				// explore all destinations...
				while(!destinations.isEmpty()){

					val nextRoute = destinations.pop()
					if (!path.toList().contains(nextRoute)){
						// if the location hasn't been explored yet, explore it
						path.push(nextRoute)
						//println(path.map{it.coord})
						explore(matrix, path)
					}
//					else{
//						// otherwise remove it...
//						//path.pop();
//						//explore(matrix, path)
//
//						println(nextRoute)
//					}
				}
				// if the destinations stack is empty we have visited all the location
				// but did not found a good route
				//path.pop();
				//if (path.isEmpty()) path.push(matrix[0][0])
				//explore(matrix, path)
				// if none of the destinations turned out to be succesfull backtrack...
				//while(path.peek() != currentLocation){
//			if(!path.isEmpty()){
//				path.pop()
//				explore(matrix, path)
//			}
			 // what if I get back to the beginning??
				//}
			//path.forEach({println(it.coord)})

			}

		}
}

fun solved(matrix: ArrayList<List<Location>>, stack: Stack<Location>): Boolean{
	return stack.peek().coord == Pair(matrix.count() - 1, matrix.count() - 1)
}

/* get a stack of all possible destinations from the current cell based on the range of movement from
  the cell itself */
fun getDestinations(matrix: ArrayList<List<Location>>, currentLocation: Location): Stack<Location>{
	val destinations = Stack<Location>()
	val row = currentLocation.coord.first
	val col = currentLocation.coord.second
	val range = currentLocation.range;
	destinations.addAll(listOf(
			createLocation(row, col + range, matrix), // right
			createLocation(row, col - range, matrix), // left
			createLocation(row - range, col, matrix), // top
			createLocation(row + range, col, matrix)) // bottom
			.filter({it != null && it.range != 0 && it.range < matrix.count()})) // get rid of invalid destinations
	return destinations
}

/* return a valid coordinate if in range otherwise a null value */
fun createLocation(row: Int, col: Int, matrix: ArrayList<List<Location>>): Location?{
	val puzzleSize = matrix.count()
	var location: Location?
	when{
		inRange(row, puzzleSize)
		&& inRange(col, puzzleSize) -> location = Location(row, col, matrix[row][col].range)
		else -> location = null
	}
	return location
}

/* check if the coordinate is in range */
fun inRange(coord: Int, size: Int): Boolean{
	return coord >= 0 && coord < size
}

