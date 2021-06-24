package cspace

import com.google.inject.Guice
import com.google.inject.Injector
import cspace.frame.MainFrame
import cspace.ui.MainView
import cspace.util.JComponentInitializer
import org.slf4j.LoggerFactory
import javax.swing.SwingUtilities

/**
 * 应用程序启动器
 * @author huobn
 */
class ApplicationStarter {

    private val log = LoggerFactory.getLogger(ApplicationStarter::class.java)

    companion object {
        @JvmStatic private val logger = LoggerFactory.getLogger(ApplicationStarter::class.java)
        @JvmStatic private var context: Injector

        // 初始化容器Context
        init {
            logger.info("Initialize application component context...")
            context = Guice.createInjector(BeansProvider())
            logger.info("Application component context start successfully!")
        }

        // 加载一下PathResolver
        init {
            Class.forName("cspace.util.PathResolver")
            Class.forName("cspace.util.AssetsResolver")
        }

        @JvmStatic fun getContext(): Injector {
            return context
        }
    }

    // 程序启动入口方法
    fun start() {
        log.info("Application starting...")
        SwingUtilities.invokeLater {
            val frame = context.getInstance(MainFrame::class.java)
            JComponentInitializer.showFrame(frame)
        }
    }
}