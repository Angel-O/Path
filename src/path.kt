import java.io.File
import java.util.Stack

fun main(args: Array<String>) {

	// create a file selector to load up the puzzle
	val fs = FileSelector()
	val file = fs.chooseFile()

	// exit if there is an error
	if(file == null){
		println("Unable to load file.")
		return
	}

	// success
	println(file.toString() + " succesfully loaded")

	// create the matrix
	val matrix = createMatrix(file)
	// cool! the values will be printed instead of the hash..
	matrix.forEach({ println(it) })

	// solve puzzle...
}

fun createMatrix(file: File) : ArrayList<List<Int>>{

	val matrix = ArrayList<List<Int>>()
	for (line in file.readLines()){

		// split each line and parse each chunk to an int
		matrix.add(line.split(" ").map{ it.toInt() })
	}
	return matrix
}

fun getMeAStack(): Stack<Int>?{

	return null;
}
