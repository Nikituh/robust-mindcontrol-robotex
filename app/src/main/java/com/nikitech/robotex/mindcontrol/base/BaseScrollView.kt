package mobile.ecofleet.com.common.base

open class BaseScrollView(context: android.content.Context) : android.widget.ScrollView(context)  {

    private var container = BaseView(context)

    init {
        super.addView(container)
    }

    var frame: CGRect = CGRect.Companion.empty

    fun setFrame(x: Int, y: Int, width: Int, height: Int) {
        this.frame = CGRect(x, y, width, height)

        val params = android.widget.RelativeLayout.LayoutParams(width, height)
        params.leftMargin = x
        params.topMargin = y

        layoutParams = params

        layoutSubviews()

//        container.layoutParams = params
    }

    fun matchParent() {
        val metrics = context.resources.displayMetrics
        setFrame(0, 0, metrics.widthPixels, metrics.heightPixels)
    }

    open fun layoutSubviews() {

    }

    override fun addView(child: android.view.View?) {
        container.addView(child)
    }

    override fun removeView(view: android.view.View?) {
        container.removeView(view)
    }

    override  fun removeAllViews() {
        container.removeAllViews()
    }
    fun getMetrics(): android.util.DisplayMetrics {
        return context.resources.displayMetrics
    }

    fun getDensity(): Float {
        return getMetrics().density
    }
}