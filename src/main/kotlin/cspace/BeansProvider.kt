package cspace

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import cspace.component.ResourceBrowserPopupMenu
import cspace.dialog.ExceptionDialog
import cspace.dialog.ResourceDetailDialog
import cspace.frame.MainFrame
import cspace.ui.MainView
import cspace.ui.ResourceBrowserView

class BeansProvider: AbstractModule() {

    override fun configure() {
        // 注册UI组件
        bind(MainView::class.java).`in`(Scopes.SINGLETON)
        bind(ResourceBrowserView::class.java).`in`(Scopes.SINGLETON)
        bind(ResourceBrowserPopupMenu::class.java).`in`(Scopes.SINGLETON)
        bind(ExceptionDialog::class.java).`in`(Scopes.SINGLETON)
        bind(ResourceDetailDialog::class.java).`in`(Scopes.SINGLETON)
        // 注册frame
        bind(MainFrame::class.java).`in`(Scopes.SINGLETON)
    }
}