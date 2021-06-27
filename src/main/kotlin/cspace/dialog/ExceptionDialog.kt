package cspace.dialog

import cspace.ApplicationStarter
import cspace.frame.MainFrame
import cspace.util.AssetsResolver
import cspace.util.DialogSupport
import cspace.util.JComponentInitializer
import java.awt.*
import java.io.PrintStream
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Exception
import javax.swing.*

/**
 * 当程序运行出现异常时，使用这个Dialog进行显示异常的信息
 * @author huobn
 */
class ExceptionDialog: JDialog(), DialogSupport {


    // 异常消息显示的区域
    private val exceptionMessageTextArea: JTextArea by lazy {
        val textArea = JTextArea()
        textArea.lineWrap = true
        textArea.font = Font(Font.SANS_SERIF, Font.BOLD, 15) // 显示的的字体大小

        textArea.isEditable = false // 设置不可编辑
        textArea
    }

    private val messageArea: JPanel by lazy {
        val area = JPanel()
        area.layout = BorderLayout()
        area.preferredSize = Dimension(500, 100)
        area.size = Dimension(500, 100)
        area.add(exceptionMessageTextArea, BorderLayout.CENTER)
        val iconLabel = JLabel(ImageIcon(AssetsResolver.getAsset("exception-dialog.icon"))).apply {
            isOpaque = true
        }
        iconLabel.preferredSize = Dimension(60, 60)
        area.add(iconLabel, BorderLayout.WEST)
        area
    }

    private val detailInformationTextArea: JTextArea by lazy {
        val textArea = JTextArea()
        textArea.isEditable = false
        textArea.foreground = Color.RED
        textArea
    }

    private val detailInformationArea: JScrollPane by lazy {
        val scrollPane = JScrollPane(detailInformationTextArea)
        scrollPane.preferredSize = Dimension(500, 350)
        scrollPane.size = Dimension(500, 350)
        scrollPane
    }

    private val okButton: JButton by lazy {
        val button = JButton("ok")
        button.preferredSize = Dimension(80, 35)
        button.addActionListener {
            this@ExceptionDialog.isVisible = false
        }
        button
    }

    private val buttonGroup: JPanel by lazy {
        val group = JPanel()
        group.layout = FlowLayout().apply {
            hgap = 10
            vgap = 10
            alignment = FlowLayout.RIGHT
        }
        group.add(okButton)
        group
    }

    private val topLevelComponent: Box by lazy {
        val box = Box.createVerticalBox()
        box.add(Box.createVerticalStrut(10))
        box.add(messageArea)
        box.add(Box.createVerticalStrut(10))
        box.add(detailInformationArea)
        box.add(buttonGroup)
        box
    }

    init {
        preferredSize = Dimension(500, 500)
        size = Dimension(500, 500)
        isResizable = false
        title = "Error"
        contentPane = topLevelComponent
        isModal = true
        isVisible = false
        setLocationRelativeTo(null)
    }


    // ---------------------------- 业务方法 -------------------------
    fun acceptException(exception: Exception) {
        exceptionMessageTextArea.text = exception.message
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        exception.printStackTrace(printWriter)
        detailInformationTextArea.text = stringWriter.toString()

    }

    override fun isExitOnApprove(): Boolean {
        return false
    }

    override fun showDialog() {
        JComponentInitializer.alignCenter(this)
        isVisible = true
    }

    override fun closeDialog() {
       isVisible = false
    }
}