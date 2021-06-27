package cspace.component

import cspace.model.AnalyseKeyFrame
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.border.Border

/**
 * 关键帧按钮
 * @author huobn
 */
class KeyFrameButton(sequenceNumber: Int, analyseKeyFrame: AnalyseKeyFrame): JButton() {

    private val sequenceNumberLabel: JLabel by lazy {
        val label = JLabel()
        label.font = Font(Font.SANS_SERIF, Font.BOLD, 16)
        label.text = sequenceNumber.toString()
        label
    }

    private val frameNameLabel: JLabel by lazy {
        val label = JLabel()
        label
    }

    var keyFrame: AnalyseKeyFrame = analyseKeyFrame

    var sequenceNumber: Int = sequenceNumber
    set(value) {
        field = value
        sequenceNumberLabel.text = value.toString()
    }

    init {
        size = Dimension(80, 80)
        preferredSize = Dimension(80, 80)
        layout = BorderLayout()

        add(sequenceNumberLabel, BorderLayout.NORTH)
        add(frameNameLabel, BorderLayout.CENTER)
    }

    // ------------------------ 业务方法 --------------------------

    fun refreshUI() {
        sequenceNumberLabel.text = sequenceNumber.toString()
        frameNameLabel.text = keyFrame.name
    }

    fun acceptKeyFrame(analyseKeyFrame: AnalyseKeyFrame) {
        keyFrame = analyseKeyFrame
        frameNameLabel.text = analyseKeyFrame.name
    }

}