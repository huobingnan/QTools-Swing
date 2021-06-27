package cspace.util

interface DialogSupport {

    fun isExitOnApprove(): Boolean

    fun showDialog(): Unit

    fun closeDialog(): Unit
}