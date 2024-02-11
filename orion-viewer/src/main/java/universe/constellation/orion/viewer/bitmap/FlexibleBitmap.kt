package universe.constellation.orion.viewer.bitmap

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import universe.constellation.orion.viewer.BitmapCache
import universe.constellation.orion.viewer.document.Document
import universe.constellation.orion.viewer.geometry.RectF
import universe.constellation.orion.viewer.layout.LayoutPosition
import universe.constellation.orion.viewer.log

data class PagePart(val absPartRect: Rect, @Volatile var bitmap: Bitmap? = null) {

    private var drawTmp = Rect()
    private var rendTmp = Rect()
    private var sceneTmp = RectF()

    private fun initBitmapIfNeeded(bitmapCache: BitmapCache): Bitmap {
        if (bitmap == null) {
            bitmap = bitmapCache.createBitmap(absPartRect.width(), absPartRect.height())
        }
        return bitmap!!
    }

    fun render(requestedArea: Rect, zoom: Double, cropLeft: Int, cropTop: Int, page: Int, doc: Document, bitmapCache: BitmapCache) {
        rendTmp.set(absPartRect)
        if (rendTmp.intersect(requestedArea)) {
            rendTmp.offset(-absPartRect.left, -absPartRect.top)
            renderInner(rendTmp, zoom, absPartRect.left + cropLeft, absPartRect.top + cropTop, page, doc, initBitmapIfNeeded(bitmapCache))
        }
    }

    fun draw(canvas: Canvas, pageVisiblePart: Rect, scene: RectF, defaultPaint: Paint, borderPaint: Paint) {
        drawTmp.set(absPartRect)
        if (bitmap != null && drawTmp.intersect(pageVisiblePart)) {
            val deltaX = -(pageVisiblePart.left - scene.left)
            val deltaY = -(pageVisiblePart.top - scene.top)
            sceneTmp.set(drawTmp)
            sceneTmp.offset(deltaX, deltaY)
            drawTmp.offset(-absPartRect.left, -absPartRect.top)
            canvas.drawBitmap(bitmap!!, drawTmp, sceneTmp, defaultPaint)
            log("DrawPart: $drawTmp $sceneTmp")
        }
    }

    fun free(bitmapCache: BitmapCache) {
        bitmap?.let {
            bitmapCache.free(it)
        }
        bitmap = null
    }
}

class FlexibleBitmap(initialArea: Rect, private val partWidth: Int, private val partHeight: Int) {
    private var renderingArea = initialArea
        private set

    private var data = initData(renderingArea.width(), renderingArea.height())

    val width: Int
        get() =  renderingArea.width()

    val height: Int
        get() =  renderingArea.height()

    private fun initData(width: Int, height: Int) = Array(height / partHeight + 1) { row ->
        Array(width / partWidth + 1) { col ->
            val left = partWidth * col
            val top = partHeight * row
            PagePart(
                Rect(
                    left,
                    top,
                    left + partWidth,
                    top + partHeight
                )
            )
        }
    }

    fun resize(width: Int, height: Int, bitmapCache: BitmapCache): FlexibleBitmap {
        free(bitmapCache)
        data = initData(width, height)
        renderingArea.set(0, 0, width, height)
        return this
    }

    fun render(renderingArea: Rect, curPos: LayoutPosition, page: Int, doc: Document, bitmapCache: BitmapCache) {
        val left = renderingArea.left / partWidth
        val top = renderingArea.top / partHeight
        val right = renderingArea.rightInc / partWidth
        val bottom = renderingArea.bottomInc / partHeight

        for (r in top..bottom) {
            for (c in left..right) {
                data[r][c].render(renderingArea, curPos.docZoom, curPos.x.marginLess, curPos.y.marginLess, page, doc, bitmapCache)
            }
        }
    }

    fun draw(canvas: Canvas, srcRect: Rect, scene: RectF, defaultPaint: Paint, borderPaint: Paint) {
        for (r in data) {
            for (c in r) {
                c.draw(canvas, srcRect, scene, defaultPaint, borderPaint)
            }
        }
    }

    fun free(cache: BitmapCache) {
        for (r in data) {
            for (c in r) {
                c.free(cache)
            }
        }
    }

    fun bitmaps(): List<Bitmap> {
        val res = mutableListOf<Bitmap>()
        for (r in data) {
            for (c in r) {
                c.bitmap?.let { res.add(it) }
            }
        }
        return res
    }

    fun parts(): List<PagePart> {
        val res = mutableListOf<Bitmap>()
        return data.flatMap { it.map { it } }
    }
}

val Rect.rightInc get() = right - 1
val Rect.bottomInc get() = bottom - 1

private fun renderInner(bound: Rect, curPos: LayoutPosition, page: Int, doc: Document, bitmap: Bitmap): Bitmap {
    println("Rendering $page: $bound $curPos")
    doc.renderPage(page, bitmap, curPos.docZoom, bound.left, bound.top,  bound.right, bound.bottom, curPos.x.marginLess, curPos.y.marginLess)
    return bitmap
}

private fun renderInner(bound: Rect, zoom: Double, offsetX: Int, offsetY: Int, page: Int, doc: Document, bitmap: Bitmap): Bitmap {
    println("Rendering $page: $bound $offsetX $offsetY")
    doc.renderPage(
        page,
        bitmap,
        zoom,
        bound.left,
        bound.top,
        bound.right,
        bound.bottom,
        offsetX,
        offsetY

    )
    return bitmap
}