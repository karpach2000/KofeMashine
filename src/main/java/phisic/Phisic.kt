package phisic

import com.pi4j.io.gpio.*
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent
import com.pi4j.io.gpio.event.GpioPinListenerDigital
import com.pi4j.util.CommandArgumentParser
import com.pi4j.io.gpio.GpioPinDigitalInput
import com.pi4j.io.gpio.Pin
import com.pi4j.io.gpio.PinPullResistance
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.io.gpio.GpioPinDigitalOutput









object Interfaces
{
    fun getButtonPin (buttonNumber : Int) : Pin
    {


    }

    fun getRelePin (buttonNumber : Int) : Pin
    {


    }
}




class Board
{


    val buttons =arrayListOf(
        Button(0),
        Button(1),
        Button(2),
        Button(3),
        Button(4),
        Button(5))

    val reles =arrayListOf(
        Rele(0),
        Rele(1),
        Rele(2),
        Rele(3),
        Rele(4),
        Rele(5),
        Rele(6),
        Rele(7),
        Rele(8),
        Rele(9),
        Rele(10),
        Rele(11),
        Rele(12),
        Rele(13),
        Rele(14)
    )







}



class Button(val buttonNumber: Int)
{

    private val gpio = GpioFactory.getInstance()
    private var pin: Pin? = null
    private var button: GpioPinDigitalInput? = null

    val reles =  ArrayList<Rele>()

    fun generate()
    {
        //инициализвция кнопки
        pin = CommandArgumentParser.getPin(RaspiPin::class.java, Interfaces.getButtonPin(buttonNumber))
        val pull = CommandArgumentParser.getPinPullResistance(PinPullResistance.PULL_UP)
        button = gpio.provisionDigitalInputPin(pin, pull)
    }



}




/**
 * Класс описывающий поведение реле.
 */
class Rele(val releNumber: Int)
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
     * Через какое время включится.
     */
    var timeOn = 0L
    /**
     * Через какое время выключится.
     */
    var timeOff = 0L

    /**
     * Инициализировать.
     */
    fun generate()
    {
        pin = gpio.provisionDigitalOutputPin(Interfaces.getRelePin(releNumber))
    }

    fun action()
    {
        var thread = Thread(Runnable {
            Thread.sleep(timeOn)
            open()
            Thread.sleep(timeOff)
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