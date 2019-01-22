import com.parcel.Board
import com.parcel.license.LicenseChecker

fun main(){

    if(LicenseChecker.checkLicense()) {
        println("Лицензия принята. Запуск программы")

        val board = Board()
        board.update()
        board.generate()
        while(true)
            Thread.sleep(100)
    } else {
        println("Лицензия не принята. Заплатите разработчикам программы, чтобы получить лицензию")
    }


}