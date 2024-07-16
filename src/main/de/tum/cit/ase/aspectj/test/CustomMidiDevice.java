package de.tum.cit.ase.aspectj.test;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.sound.midi.MidiUnavailableException;
import java.util.List;
import java.util.ArrayList;

public class CustomMidiDevice implements MidiDevice {
    private Info info = new Info("Custom MIDI Device", "ExampleVendor", "A custom MIDI device", "1.0") {};
    private List<Receiver> receivers = new ArrayList<>();
    private List<Transmitter> transmitters = new ArrayList<>();

    @Override
    public Info getDeviceInfo() {
        return info;
    }

    @Override
    public void open() throws MidiUnavailableException {
        System.out.println("Custom MIDI Device opened.");
    }

    @Override
    public void close() {
        System.out.println("Custom MIDI Device closed.");
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public long getMicrosecondPosition() {
        return 0;
    }

    @Override
    public int getMaxReceivers() {
        return -1; // Unlimited receivers
    }

    @Override
    public int getMaxTransmitters() {
        return -1; // Unlimited transmitters
    }

    @Override
    public Receiver getReceiver() throws MidiUnavailableException {
        Receiver receiver = new Receiver() {
            @Override
            public void send(javax.sound.midi.MidiMessage message, long timeStamp) {
                System.out.println("Message received: " + message);
            }

            @Override
            public void close() {
                System.out.println("Receiver closed.");
            }
        };
        receivers.add(receiver);
        return receiver;
    }

    @Override
    public List<Receiver> getReceivers() {
        return receivers;
    }

    @Override
    public Transmitter getTransmitter() throws MidiUnavailableException {
        Transmitter transmitter = new Transmitter() {
            @Override
            public void setReceiver(Receiver receiver) {
                System.out.println("Transmitter set receiver.");
            }

            @Override
            public Receiver getReceiver() {
                return null;
            }

            @Override
            public void close() {
                System.out.println("Transmitter closed.");
            }
        };
        transmitters.add(transmitter);
        return transmitter;
    }

    @Override
    public List<Transmitter> getTransmitters() {
        return transmitters;
    }
}

