/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiotransmitter;

import javax.sound.sampled.AudioFormat;

/**
 *
 * @author nathanr
 */
public class AudioSettings
{
    	public static AudioFormat getAudioFormat() {
        float sampleRate = 22000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                             channels, signed, bigEndian);
        return format;
    } 
}
