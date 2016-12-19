package com.tim.bong.util;

/**
 * Created by Tim on 18.12.2016.
 */
public class MathUtils2 {
    //http://yal.cc/angular-rotations-explained/
    public static float cycle(float value, float min, float max) {
        float delta = (max - min);
        // % is remainder-of-division operator here.
        // limit input to (-delta .. +delta):
        float result = (value - min) % delta;
        // wrap negative results around the limit:
        if (result < 0) result += delta;
        // return adjusted input:
        return min + result;
    }
}
