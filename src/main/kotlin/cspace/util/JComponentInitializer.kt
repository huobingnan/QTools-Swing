package cspace.util

import java.awt.Point
import java.awt.Toolkit
import java.awt.Window
import javax.swing.JComponent
import javax.swing.JDialog
import javax.swing.JFrame
import kotlin.math.abs

/**
 * 对一些JComponent进行简单的初始化工作，避免重复写相同的代码
 */
class JComponentInitializer {

    companion object {
        @JvmStatic fun frameCommonInitialization(frame: JFrame): Unit {
            frame.isVisible = false
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.setLocationRelativeTo(null) // 设置居中显示
        }

        @JvmStatic fun showDialog(dialog: JDialog): Unit {
            dialog.isVisible = true
        }

        @JvmStatic fun showFrame(frame: JFrame): Unit {
            frame.isVisible = true
        }

        @JvmStatic fun closeFrame(frame: JFrame): Unit {
            frame.isVisible = false
        }

        // 使用这个方法显示Dialog，会调用Dialog的showDialog方法来显示dialog
        // 方便将一些初始化代码逻辑写在这里
        @JvmStatic fun showDialogSupport(dialogSupport: DialogSupport): Unit {
            dialogSupport.showDialog()
        }

        // 使窗体居中显示
        @JvmStatic fun alignCenter(component: Window): Unit {
            // 得到显示器屏幕的宽高
            val width = Toolkit.getDefaultToolkit().screenSize.width;
            val height = Toolkit.getDefaultToolkit().screenSize.height;
            val windowWidth = component.width
            val windowHeight = component.height
            component.location = Point((width - windowWidth) / 2, (height - windowHeight) / 2)
        }


        // 还有BUG，不能使用
        @JvmStatic fun alignCenter(parent: Window, component: Window): Unit {
            // 获取父容器坐标
            val px = parent.location.x
            val py = parent.location.y
            // 获取父容器宽高
            val pWidth = parent.width
            val pHeight = parent.height
            // 获取子容器宽高
            val cWidth = component.width
            val cHeight = component.height
            println(px)
            println(py)
            component.location = Point(px + (abs(pWidth - cWidth) / 2), py + (abs(pHeight - cHeight) / 2))
        }
    }
}