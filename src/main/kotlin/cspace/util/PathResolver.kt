package cspace.util

import org.slf4j.LoggerFactory

class PathResolver {
    companion object {

        private val log = LoggerFactory.getLogger(PathResolver::class.java)

        @JvmStatic val APP_RUNNING_FOLDER: String = System.getProperty("user.dir")

        init {
            log.info("Application running folder : {}", APP_RUNNING_FOLDER)
        }
        @JvmStatic val ASSET_FOLDER: String = "${APP_RUNNING_FOLDER}/asset"

        init {
            log.info("Application asset folder : {}", ASSET_FOLDER)
        }
    }
}