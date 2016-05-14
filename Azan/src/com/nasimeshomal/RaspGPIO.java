package com.nasimeshomal;

import com.pi4j.io.gpio.*;

/**
 * Created by Mahmood on 3/17/2016.
 */
public class RaspGPIO {
    private static RaspGPIO ourInstance = new RaspGPIO();

    public static RaspGPIO getInstance() {
        return ourInstance;
    }

    public GpioPinDigitalOutput getSpeakerPin() {
        return speakerPin;
    }

    GpioPinDigitalOutput speakerPin;

    public GpioPinDigitalOutput getPowerPin() {
        return powerPin;
    }

    GpioPinDigitalOutput powerPin;

    public void TurnOnAmp() throws InterruptedException {
        this.powerPin.high();
        Thread.sleep(2*1000);
        this.speakerPin.pulse(1000,true);
    }

    public void TurnOffAmp()
    {
        this.powerPin.low();
    }

    private RaspGPIO() {
        GpioController gpio = GpioFactory.getInstance();
        speakerPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Speaker", PinState.LOW);
        powerPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "Power", PinState.LOW);
        speakerPin.setShutdownOptions(true, PinState.LOW);
        powerPin.setShutdownOptions(true, PinState.LOW);
    }
}
