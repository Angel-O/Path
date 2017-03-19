/* represents a puzzle's location */
data class Location(val row:Int, val col:Int, val range:Int){

	val coord = Pair(row, col) // coordinates
	val value = range // value of the cell (move range)
}