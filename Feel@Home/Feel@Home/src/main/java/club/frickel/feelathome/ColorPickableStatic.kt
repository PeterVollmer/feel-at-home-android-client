package club.frickel.feelathome


import java.util.*

class ColorPickableStatic internal constructor( override var effect: Effect) : ColorPickable {

    override fun setColor(color: Int) {

        var effectConfig: MutableMap<String, Any> = HashMap()
        if (effect.config != null) {
            effectConfig = effect.config.toMutableMap()
            effectConfig.put("Color", "#" + Integer.toHexString(color).substring(2))
        }
        effect.config = effectConfig
    }

    override fun getColor(): Int {
        if (effect.config != null) {
            return Integer.parseInt(effect.config["Color"].toString().substring(1), 16) or 0xFF000000.toInt()
        }
        return 0
    }
}
