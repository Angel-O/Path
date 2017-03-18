import java.io.File

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
	val matrix = ArrayList<List<Int>>()
	for (line in file.readLines()){

		// split each line and parse each chunk to an int
		matrix.add(line.split(" ").map{ it.toInt() })
	}

	for(line in matrix){
		println(line) // cool! the values will be printed instead of the hash..
	}
}
