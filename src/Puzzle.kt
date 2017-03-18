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
	stack.push(puzzle[0][8])
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
	val move = path.peek().move

	// get the possible locations
	val destinations = getDestinations(matrix, move)
}


fun getDestinations(matrix: ArrayList<List<Location>>, move: Int){

}

