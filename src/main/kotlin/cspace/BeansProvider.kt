package cspace

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import cspace.component.ExtSettingNewPopupMenu
import cspace.component.ResourceBrowserPopupMenu
import cspace.dialog.*
import cspace.frame.MainFrame
import cspace.graphic.BondLengthTableViewBuilder
import cspace.ui.ChannelView
import cspace.ui.DisplayView
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
        bind(ChannelView::class.java).`in`(Scopes.SINGLETON)
        bind(DisplayView::class.java).`in`(Scopes.SINGLETON)
        bind(ChannelOptionDialog::class.java).`in`(Scopes.SINGLETON)
        bind(ExtSettingNewPopupMenu::class.java).`in`(Scopes.SINGLETON)
        bind(FrameOptionDialog::class.java).`in`(Scopes.SINGLETON)
        bind(ChannelOptionNewExtSettingDialog::class.java).`in`(Scopes.SINGLETON)
        // 注册frame
        bind(MainFrame::class.java).`in`(Scopes.SINGLETON)
        // 注册Graph Builder
        bind(BondLengthTableViewBuilder::class.java).`in`(Scopes.SINGLETON)
    }
}