package cspace.util

import org.slf4j.LoggerFactory

class AssetsResolver {
    companion object {
        private val log = LoggerFactory.getLogger(AssetsResolver::class.java)

        private val assetRegistry = HashMap<String, String>()

        init {
            log.info("Register application assets...")
            assetRegistry["exception-dialog.icon"] = "${PathResolver.ASSET_FOLDER}/exception-dialog.icon.png"
        }

        fun getAsset(assetName: String): String {
            return assetRegistry[assetName]!!
        }
    }
}