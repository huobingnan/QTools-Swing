package cspace.util

import javax.swing.JDialog
import javax.swing.JFrame

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
    }
}