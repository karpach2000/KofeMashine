package phisic

import com.pi4j.io.gpio.*
import com.pi4j.util.CommandArgumentParser
import com.pi4j.io.gpio.GpioPinDigitalInput
import com.pi4j.io.gpio.Pin
import com.pi4j.io.gpio.PinPullResistance
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.io.gpio.GpioPinDigitalOutput
import java.util.ArrayList
import sun.nio.cs.Surrogate.isHigh
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent
import com.pi4j.io.gpio.event.GpioPinListenerDigital



/**
 * Наша плата.
 */
class Board
{


    val buttons =arrayListOf(
        Button(0, arrayListOf( Rele(0, 1, 3),Rele(0, 2, 1),Rele(0, 4, 8))),
        Button(1, arrayListOf( Rele(0, 1, 3),Rele(0, 2, 1),Rele(0, 4, 8))),
        Button(2, arrayListOf( Rele(0, 1, 3),Rele(0, 2, 1),Rele(0, 4, 8))),
        Button(3, arrayListOf( Rele(0, 1, 3),Rele(0, 2, 1),Rele(0, 4, 8))),
        Button(4, arrayListOf( Rele(0, 1, 3),Rele(0, 2, 1),Rele(0, 4, 8)))
    )


    fun generate()
    {
        for(b in buttons)
            b.generate()
    }

}

/**
 * Слушатель события нажатия кнопки.
 */
interface RpiButtonListener {
    fun buttonPositionChanged(pin: Pin, value: Boolean)
}


/**
 * Кнопка.
 * @param buttonNumber  ноомер кнопки по ТЗ
 * @param reles релюшки))
 */
class Button(val buttonNumber: Int, val reles: ArrayList<Rele>)
{

    private val gpio = GpioFactory.getInstance()
    private lateinit var pin: Pin
    private lateinit var button: GpioPinDigitalInput

    private val listeners = ArrayList<RpiButtonListener>()

    var value = true;


    /**
     * Инициализируем железо.
     */
    fun generate()
    {
        //инициализвция кнопки
        pin = CommandArgumentParser.getPin(RaspiPin::class.java, Interfaces.getButtonPin(buttonNumber))
        val pull = CommandArgumentParser.getPinPullResistance(PinPullResistance.PULL_UP)
        button = gpio.provisionDigitalInputPin(pin, pull)

        //инициализация релюх
        for (r in reles)
        {
            r.generate()
        }

        //подписка на события
        this.button.addListener(GpioPinListenerDigital {
            //value = this.button.isHigh()
            if(this.button.isHigh())
                for (r in reles)
                    r.action()
        })
    }



}




/**
 * Класс описывающий поведение реле.
 * @param releNumber номе реле
 * @param timeOn время через которое реле включается
 * @param timeJob время через которое реле выключается
 */
class Rele(val releNumber: Int, val timeOn: Long, val timeJob: Long)
{

    private val gpio = GpioFactory.getInstance()
    private lateinit var pin : GpioPinDigitalOutput



    /**
     * Инверсия пина.
     */
    var iverse = false
    /**
     * Имя реле.
     */
    var name = "Напишите сюда имя реле что бы не путаться."


    /**
     * Инициализировать.
     */
    fun generate()
    {
        pin = gpio.provisionDigitalOutputPin(Interfaces.getRelePin(releNumber))
    }

    /**
     * Отработать алгоритм
     */
    fun action()
    {
        var thread = Thread(Runnable {
            Thread.sleep(timeOn)
            open()
            Thread.sleep(timeJob)
            close()

        })
        thread.start()
    }

    /**
     * Открыит реле.
     */
    private fun open() = if(!iverse) pin.high() else pin.low()

    /**
     * Закрыть реле.
     */
    private fun close() =   if(!iverse) pin.low() else pin.high()

}