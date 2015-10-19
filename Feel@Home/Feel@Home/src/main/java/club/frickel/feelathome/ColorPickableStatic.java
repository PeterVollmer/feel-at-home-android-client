package club.frickel.feelathome;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Peter Vollmer
 * Date: 2/3/14
 * Time: 3:44 PM
 */
public class ColorPickableStatic implements ColorPickable {

    Effect effect;

    ColorPickableStatic(Effect effect){
        this.effect = effect;

    }

    @Override
    public void setColor(int color) {

        Map<String, Object> effectConfig = new HashMap<>();
        if (effect.getConfig() != null) {
            effectConfig = effect.getConfig();
            effectConfig.put("Color", "#" + Integer.toHexString(color).substring(2));
        }
        effect.setConfig(effectConfig);
    }

    @Override
    public int getColor() {
        if (effect.getConfig() != null) {
            return Integer.parseInt((effect.getConfig()).get("Color").toString().substring(1), 16) | 0xFF000000;
        }
        return 0;
    }

    @Override
    public Effect getEffect() {
        return effect;
    }
}
