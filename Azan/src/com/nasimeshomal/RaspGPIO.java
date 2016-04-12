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

    private RaspGPIO() {
        GpioController gpio = GpioFactory.getInstance();
        speakerPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Speaker", PinState.LOW);
        speakerPin.setShutdownOptions(true, PinState.LOW);
    }
}
