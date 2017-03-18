import kotlin.collections.*;

fun main(args: Array<String>) {
	//System.out.println("")
	println("this is kotlin")
	val number = 56
	val myList = listOf(1, 2, 3)

	val g = ggg() // not working

	myList.forEach { println(it) }

	println("the value of twice $number is ${double(number)}")

	println(g())

	//val myStack = Stack<Int>(1, 4, 5)

	val matrix = listOf<List<Int>>(

		listOf<Int>(7, 4, 4, 6, 6, 3 , 2, 2, 6, 8),
		listOf<Int>(3, 3, 6, 5, 4, 3 , 7, 2, 8, 3),
		listOf<Int>(7, 4, 4, 6, 6, 3 , 2, 2, 6, 8),
		listOf<Int>(7, 4, 4, 6, 6, 3 , 2, 2, 6, 8),
		listOf<Int>(7, 4, 4, 6, 6, 3 , 2, 2, 6, 8),
		listOf<Int>(7, 4, 4, 6, 6, 3 , 2, 2, 6, 8),
		listOf<Int>(7, 4, 4, 6, 6, 3 , 2, 2, 6, 8),
		listOf<Int>(7, 4, 4, 6, 6, 3 , 2, 2, 6, 8),
		listOf<Int>(7, 4, 4, 6, 6, 3 , 2, 2, 6, 8),
		listOf<Int>(7, 4, 4, 6, 6, 3 , 2, 2, 6, 8)
	)

	//val mm = arrayOf<arrayOf<Int>>()

}

fun double(number: Int) = 2 * number

fun ggg() = { // with equal we are 'assigning' a Unit value to ggg: nothing will happen when it gets called

	val myList = listOf(1, 2, 3)

	myList.forEach { x -> println(x) }
}

fun iii(){ // without equal it will print the list content

	val myList = listOf(1, 2, 3)

	myList.forEach { x -> println(x) }
}


