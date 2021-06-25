package cspace.util

import org.slf4j.LoggerFactory

/**
 * 应用资源处理器
 * @author huobn
 */
class AssetsResolver {
    companion object {
        private val log = LoggerFactory.getLogger(AssetsResolver::class.java)

        private val assetRegistry = HashMap<String, String>()

        init {
            log.info("Register application assets...")
            assetRegistry["exception-dialog.icon"] = "${PathResolver.ASSET_FOLDER}/exception-dialog.icon.png"
            assetRegistry["channel-view.new-channel.icon"] = "${PathResolver.ASSET_FOLDER}/channel-view.new-channel.icon.png"
            assetRegistry["channel-view.execute.icon"] = "${PathResolver.ASSET_FOLDER}/channel-view.execute.icon.png"
            assetRegistry["channel-view.setting.icon"] = "${PathResolver.ASSET_FOLDER}/channel-view.setting.icon.png"
            assetRegistry["channel-view.delete.icon"] = "${PathResolver.ASSET_FOLDER}/channel-view.delete.icon.png"
        }

        fun getAsset(assetName: String): String {
            return assetRegistry[assetName]!!
        }
    }
}