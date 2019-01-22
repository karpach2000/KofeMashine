import  com.parcel.Board
fun main(){
    val board = Board()
    board.update()
    board.generate()
    while(true)
        Thread.sleep(100)
}