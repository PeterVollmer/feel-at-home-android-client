package club.frickel.feelathome


import java.io.Serializable

interface ColorPickable : Serializable {
    val effect: Effect
    abstract fun setColor(color: Int)
    abstract fun getColor(): Int

}