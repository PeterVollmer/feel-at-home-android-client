package club.frickel.feelathome;

import java.io.Serializable;

/**
 * User: Peter Vollmer
 * Date: 2/3/14
 * Time: 3:34 PM
 */
public interface ColorPickable extends Serializable{
    void  setColor (int color);
    int getColor ();
    Effect getEffect();

}
