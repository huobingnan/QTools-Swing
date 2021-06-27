package cspace.model

import java.lang.IllegalArgumentException

data class AnalyseKeyFrame(
    var name: String,
    var resourceName: String
) {

    companion object {
        @JvmStatic fun validate(frame: AnalyseKeyFrame) {
            if (frame.name.isEmpty() || frame.name.isBlank())
                throw IllegalArgumentException("frame name can't empty!")
            if (frame.resourceName.isEmpty() || frame.resourceName.isBlank())
                throw IllegalArgumentException("you must choose a resource!")
        }
    }
}
