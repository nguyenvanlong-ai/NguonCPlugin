package com.nguonc
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addActors
import com.lagradost.cloudstream3.LoadResponse.Companion.addImdbId
import com.lagradost.cloudstream3.LoadResponse.Companion.addTmdbId
import com.lagradost.cloudstream3.network.WebViewResolver
import com.lagradost.cloudstream3.plugins.BasePlugin
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import org.jsoup.Jsoup
import java.net.URI
import java.net.URLEncoder
@CloudstreamPlugin
class NguonCPlugin : BasePlugin() {
    override fun load() {
        registerMainAPI(NguonCProvider())
    }
}
@JsonIgnoreProperties(ignoreUnknown = true)
data class NguonCListResponse(
    @JsonProperty("status") val status: String? = null,
    @JsonProperty("paginate") val paginate: NguonCPaginate? = null,
    @JsonProperty("items") val items: List<NguonCFilmItem>? = emptyList()
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class NguonCPaginate(
    @JsonProperty("current_page") val currentPage: Int? = 1,
    @JsonProperty("total_page") val totalPage: Int? = 1,
    @JsonProperty("total_items") val totalItems: Int? = 0,
    @JsonProperty("items_per_page") val itemsPerPage: Int? = 10
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class NguonCFilmItem(
    @JsonProperty("id") val id: String? = null,
    @JsonProperty("name") val name: String? = null,
    @JsonProperty("slug") val slug: String? = null,
    @JsonProperty("original_name") val originalName: String? = null,
    @JsonProperty("thumb_url") val thumbUrl: String? = null,
    @JsonProperty("thumb_url_webp") val thumbUrlWebp: String? = null,
    @JsonProperty("poster_url") val posterUrl: String? = null,
    @JsonProperty("poster_url_webp") val posterUrlWebp: String? = null,
    @JsonProperty("description") val description: String? = null,
    @JsonProperty("total_episodes") val totalEpisodes: Int? = null,
    @JsonProperty("current_episode") val currentEpisode: String? = null,
    @JsonProperty("time") val time: String? = null,
    @JsonProperty("quality") val quality: String? = null,
    @JsonProperty("language") val language: String? = null,
    @JsonProperty("director") val director: String? = null,
    @JsonProperty("casts") val casts: String? = null,
    @JsonProperty("year") val year: String? = null,
    @JsonProperty("tmdb") val tmdb: NguonCTmdb? = null,
    @JsonProperty("imdb") val imdb: NguonCImdb? = null
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class NguonCDetailResponse(
    @JsonProperty("status") val status: String? = null,
    @JsonProperty("movie") val movie: NguonCMovieDetail? = null
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class NguonCMovieDetail(
    @JsonProperty("id") val id: String? = null,
    @JsonProperty("name") val name: String? = null,
    @JsonProperty("slug") val slug: String? = null,
    @JsonProperty("original_name") val originalName: String? = null,
    @JsonProperty("thumb_url") val thumbUrl: String? = null,
    @JsonProperty("thumb_url_webp") val thumbUrlWebp: String? = null,
    @JsonProperty("poster_url") val posterUrl: String? = null,
    @JsonProperty("poster_url_webp") val posterUrlWebp: String? = null,
    @JsonProperty("description") val description: String? = null,
    @JsonProperty("total_episodes") val totalEpisodes: Int? = null,
    @JsonProperty("current_episode") val currentEpisode: String? = null,
    @JsonProperty("time") val time: String? = null,
    @JsonProperty("quality") val quality: String? = null,
    @JsonProperty("language") val language: String? = null,
    @JsonProperty("director") val director: String? = null,
    @JsonProperty("casts") val casts: String? = null,
    @JsonProperty("year") val year: String? = null,
    @JsonProperty("category") val category: Map<String, NguonCCategoryGroup>? = null,
    @JsonProperty("tmdb") val tmdb: NguonCTmdb? = null,
    @JsonProperty("imdb") val imdb: NguonCImdb? = null,
    @JsonProperty("episodes") val episodes: List<NguonCEpisodeServer>? = emptyList()
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class NguonCCategoryGroup(
    @JsonProperty("group") val group: NguonCGroupInfo? = null,
    @JsonProperty("list") val list: List<NguonCCategoryItem>? = emptyList()
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class NguonCGroupInfo(
    @JsonProperty("id") val id: String? = null,
    @JsonProperty("name") val name: String? = null
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class NguonCCategoryItem(
    @JsonProperty("id") val id: String? = null,
    @JsonProperty("name") val name: String? = null
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class NguonCTmdb(
    @JsonProperty("id") val id: Int? = null,
    @JsonProperty("type") val type: String? = null,
    @JsonProperty("season") val season: Int? = null,
    @JsonProperty("url") val url: String? = null
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class NguonCImdb(
    @JsonProperty("id") val id: String? = null,
    @JsonProperty("url") val url: String? = null
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class NguonCEpisodeServer(
    @JsonProperty("server_name") val serverName: String? = null,
    @JsonProperty("items") val items: List<NguonCEpisodeItem>? = emptyList()
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class NguonCEpisodeItem(
    @JsonProperty("name") val name: String? = null,
    @JsonProperty("slug") val slug: String? = null,
    @JsonProperty("embed") val embed: String? = null,
    @JsonProperty("m3u8") val m3u8: String? = null
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class NguonCLinkData(
    @JsonProperty("serverName") val serverName: String,
    @JsonProperty("episodeName") val episodeName: String? = null,
    @JsonProperty("m3u8") val m3u8: String? = null,
    @JsonProperty("embed") val embed: String? = null
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class StreamCObfuscatedData(
    @JsonProperty("sUb") val sub: String? = null,
    @JsonProperty("hD") val hd: String? = null
)
class NguonCProvider : MainAPI() {
    override var mainUrl = "https://phim.nguonc.com"
    override var name = "NguonC"
    override val hasMainPage = true
    override var lang = "vi"
    override val hasQuickSearch = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.Anime,
        TvType.AsianDrama
    )
    companion object {
        private const val USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"
    }
    private val apiHeaders by lazy {
        mapOf(
            "Accept" to "application/json",
            "User-Agent" to USER_AGENT,
            "Referer" to "$mainUrl/"
        )
    }
    override val mainPage = mainPageOf(
        "$mainUrl/api/films/phim-moi-cap-nhat?page=" to "Phim Mới Cập Nhật",
        "$mainUrl/api/films/danh-sach/phim-bo?page=" to "Phim Bộ",
        "$mainUrl/api/films/danh-sach/phim-le?page=" to "Phim Lẻ",
        "$mainUrl/api/films/danh-sach/tv-shows?page=" to "TV Shows",
        "$mainUrl/api/films/the-loai/hoat-hinh?page=" to "Hoạt Hình",
        "$mainUrl/api/films/ngon-ngu/vietsub?page=" to "Phim Vietsub",
        "$mainUrl/api/films/ngon-ngu/thuyet-minh?page=" to "Phim Thuyết Minh",
        "$mainUrl/api/films/ngon-ngu/long-tieng?page=" to "Phim Lồng Tiếng",
        "$mainUrl/api/films/the-loai/hanh-dong?page=" to "Hành Động",
        "$mainUrl/api/films/the-loai/tinh-cam?page=" to "Tình Cảm",
        "$mainUrl/api/films/the-loai/co-trang?page=" to "Cổ Trang",
        "$mainUrl/api/films/the-loai/kinh-di?page=" to "Kinh Dị",
        "$mainUrl/api/films/the-loai/khoa-hoc-vien-tuong?page=" to "Khoa Học Viễn Tưởng",
        "$mainUrl/api/films/quoc-gia/han-quoc?page=" to "Phim Hàn Quốc",
        "$mainUrl/api/films/quoc-gia/trung-quoc?page=" to "Phim Trung Quốc",
        "$mainUrl/api/films/quoc-gia/my?page=" to "Phim Âu Mỹ",
        "$mainUrl/api/films/quoc-gia/nhat-ban?page=" to "Phim Nhật Bản",
        "$mainUrl/api/films/quoc-gia/viet-nam?page=" to "Phim Việt Nam",
        "$mainUrl/api/films/nam-phat-hanh/2026?page=" to "Phim Năm 2026",
        "$mainUrl/api/films/nam-phat-hanh/2025?page=" to "Phim Năm 2025"
    )
    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = "${request.data}$page"
        val responseText = app.get(url, headers = apiHeaders).text
        val parsed = tryParseJson<NguonCListResponse>(responseText)
        val items = parsed?.items?.mapNotNull { it.toSearchResult() } ?: emptyList()
        val hasNext = (parsed?.paginate?.currentPage ?: page) < (parsed?.paginate?.totalPage ?: page)
        return newHomePageResponse(request.name, items, hasNext = hasNext)
    }
    override suspend fun quickSearch(query: String): List<SearchResponse> = search(query)
    override suspend fun search(query: String): List<SearchResponse> {
        if (query.isBlank()) return emptyList()
        val encodedQuery = URLEncoder.encode(query.trim(), "UTF-8")
        val results = mutableListOf<SearchResponse>()
        for (page in 1..2) {
            val url = "$mainUrl/api/films/search?keyword=$encodedQuery&page=$page"
            val responseText = runCatching { app.get(url, headers = apiHeaders).text }.getOrNull() ?: break
            val parsed = tryParseJson<NguonCListResponse>(responseText) ?: break
            val pageItems = parsed.items?.mapNotNull { it.toSearchResult() } ?: emptyList()
            results.addAll(pageItems)
            val currentPage = parsed.paginate?.currentPage ?: page
            val totalPage = parsed.paginate?.totalPage ?: page
            if (currentPage >= totalPage || pageItems.isEmpty()) break
        }
        return results.distinctBy { it.url }
    }
    override suspend fun load(url: String): LoadResponse? {
        val slug = extractSlugFromUrl(url)
        val apiUrl = "$mainUrl/api/film/$slug"
        val responseText = app.get(apiUrl, headers = apiHeaders).text
        val detail = tryParseJson<NguonCDetailResponse>(responseText)?.movie ?: return null
        val title = detail.name?.takeIf { it.isNotBlank() }
            ?: detail.originalName?.takeIf { it.isNotBlank() }
            ?: return null
        val poster = resolveImage(detail.thumbUrl, detail.thumbUrlWebp, detail.posterUrl, detail.posterUrlWebp)
        val background = resolveImage(detail.posterUrl, detail.posterUrlWebp, detail.thumbUrl, detail.thumbUrlWebp)
        val cleanPlot = detail.description?.let { Jsoup.parse(it).text().trim() }
        val releaseYear = detail.year?.toIntOrNull()
        val durationMinutes = parseDurationMinutes(detail.time)
        val categoryLists = detail.category?.values.orEmpty()
        val formatNames = categoryLists
            .filter { it.group?.name?.contains("Định dạng", ignoreCase = true) == true }
            .flatMap { it.list.orEmpty() }
            .mapNotNull { it.name?.trim() }
        val genreTags = categoryLists
            .filter { it.group?.name?.contains("Định dạng", ignoreCase = true) != true }
            .flatMap { it.list.orEmpty() }
            .mapNotNull { it.name?.trim() }
            .toMutableList()
        detail.quality?.takeIf { it.isNotBlank() }?.let { genreTags.add(0, it) }
        detail.language?.takeIf { it.isNotBlank() }?.let { genreTags.add(0, it) }
        val castList = detail.casts
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() }
            .orEmpty()
        val servers = detail.episodes.orEmpty()
        val isSeries = isSeriesMovie(detail, formatNames, servers)
        if (!isSeries) {
            val linkDataList = servers.mapNotNull { server ->
                val ep = server.items?.firstOrNull() ?: return@mapNotNull null
                NguonCLinkData(
                    serverName = server.serverName ?: "Vietsub",
                    episodeName = ep.name ?: "Full",
                    m3u8 = ep.m3u8?.takeIf { it.isNotBlank() },
                    embed = ep.embed?.takeIf { it.isNotBlank() }
                )
            }
            return newMovieLoadResponse(
                name = title,
                url = apiUrl,
                type = TvType.Movie,
                dataUrl = linkDataList.toJson()
            ) {
                this.posterUrl = poster
                this.backgroundPosterUrl = background
                this.year = releaseYear
                this.plot = buildPlotWithOriginalName(detail.originalName, detail.director, cleanPlot)
                this.tags = genreTags.distinct()
                this.duration = durationMinutes
                addActors(castList)
                addTmdbId(detail.tmdb?.id?.toString())
                addImdbId(detail.imdb?.id)
            }
        } else {
            val episodeGroupMap = linkedMapOf<String, MutableList<Pair<String, NguonCEpisodeItem>>>()
            for (server in servers) {
                val serverName = server.serverName ?: "Vietsub"
                for (item in server.items.orEmpty()) {
                    val key = item.slug?.takeIf { it.isNotBlank() }
                        ?: item.name?.trim()
                        ?: continue
                    episodeGroupMap.getOrPut(key) { mutableListOf() }.add(serverName to item)
                }
            }
            val episodesList = episodeGroupMap.entries.mapIndexed { index, (_, serverItems) ->
                val firstEp = serverItems.first().second
                val epName = firstEp.name?.trim().orEmpty()
                val epNum = parseEpisodeNumber(epName) ?: (index + 1)
                val linkDataList = serverItems.map { (sName, epItem) ->
                    NguonCLinkData(
                        serverName = sName,
                        episodeName = epItem.name,
                        m3u8 = epItem.m3u8?.takeIf { it.isNotBlank() },
                        embed = epItem.embed?.takeIf { it.isNotBlank() }
                    )
                }
                val displayEpName = if (epName.startsWith("Tập", ignoreCase = true)) {
                    epName
                } else if (epName.isNotBlank()) {
                    "Tập $epName"
                } else {
                    "Tập $epNum"
                }
                newEpisode(linkDataList.toJson()) {
                    this.name = displayEpName
                    this.episode = epNum
                    this.season = detail.tmdb?.season?.takeIf { it > 0 }
                }
            }
            val tvType = when {
                genreTags.any { it.contains("Hoạt Hình", ignoreCase = true) } -> TvType.Anime
                else -> TvType.TvSeries
            }
            return newTvSeriesLoadResponse(
                name = title,
                url = apiUrl,
                type = tvType,
                episodes = episodesList
            ) {
                this.posterUrl = poster
                this.backgroundPosterUrl = background
                this.year = releaseYear
                this.plot = buildPlotWithOriginalName(detail.originalName, detail.director, cleanPlot)
                this.tags = genreTags.distinct()
                this.duration = durationMinutes
                this.showStatus = parseShowStatus(detail.currentEpisode, detail.totalEpisodes)
                addActors(castList)
                addTmdbId(detail.tmdb?.id?.toString())
                addImdbId(detail.imdb?.id)
            }
        }
    }
    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val linkItems = tryParseJson<List<NguonCLinkData>>(data).orEmpty()
        if (linkItems.isEmpty()) return false
        var foundAnyLink = false
        for (item in linkItems) {
            val sourceLabel = "$name - ${item.serverName}"
            val directM3u8 = item.m3u8?.takeIf { it.isNotBlank() }
            if (directM3u8 != null) {
                emitM3u8Link(
                    sourceName = sourceLabel,
                    m3u8Url = directM3u8,
                    referer = "$mainUrl/",
                    callback = callback
                )
                foundAnyLink = true
            }
            val embedUrl = item.embed?.takeIf { it.isNotBlank() }
            if (embedUrl != null) {
                val extracted = extractFromEmbedUrl(embedUrl, sourceLabel, subtitleCallback, callback)
                if (extracted) {
                    foundAnyLink = true
                }
            }
        }
        return foundAnyLink
    }
    private suspend fun extractFromEmbedUrl(
        embedUrl: String,
        sourceLabel: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val origin = runCatching {
            val uri = URI(embedUrl)
            "${uri.scheme}://${uri.authority ?: uri.host}"
        }.getOrDefault(mainUrl)
        val embedHeaders = mapOf(
            "User-Agent" to USER_AGENT,
            "Referer" to "$mainUrl/",
            "Origin" to mainUrl
        )
        val html = runCatching {
            app.get(embedUrl, headers = embedHeaders).text
        }.getOrNull()
        val slashEscaped = charArrayOf(92.toChar(), '/').concatToString()
        if (!html.isNullOrBlank()) {
            val obfMatch = Regex("""data-obf=["']([^"']+)["']""", RegexOption.IGNORE_CASE).find(html)
            if (obfMatch != null) {
                val decodedJson = runCatching {
                    base64Decode(obfMatch.groupValues[1])
                }.getOrNull()
                val obfData = decodedJson?.let { tryParseJson<StreamCObfuscatedData>(it) }
                val subPath = obfData?.sub?.takeIf { it.isNotBlank() } ?: obfData?.hd?.takeIf { it.isNotBlank() }
                if (subPath != null) {
                    val m3u8Url = if (subPath.startsWith("http")) subPath else "$origin/${subPath.trimStart('/')}"
                    emitM3u8Link(sourceLabel, m3u8Url, "$origin/", callback)
                    return true
                }
            }
            if (html.contains("eval(function(p,a,c,k,e,d)")) {
                val unpacked = runCatching { getAndUnpack(html) }.getOrNull().orEmpty()
                val tokenMatch = Regex("""["'](eyJoIjoi[^"']+)["']""").find(unpacked)
                if (tokenMatch != null) {
                    val m3u8Url = "$origin/${tokenMatch.groupValues[1]}"
                    emitM3u8Link(sourceLabel, m3u8Url, "$origin/", callback)
                    return true
                }
                val unpackedM3u8 = Regex("""(https?://[^"'() ]+[.]m3u8[^"'() ]*)""", RegexOption.IGNORE_CASE)
                    .find(unpacked)?.groupValues?.get(1)
                if (unpackedM3u8 != null) {
                    emitM3u8Link(sourceLabel, unpackedM3u8.replace(slashEscaped, "/"), "$origin/", callback)
                    return true
                }
            }
            val directM3u8 = Regex("""(https?://[^"'() ]+[.]m3u8[^"'() ]*)""", RegexOption.IGNORE_CASE)
                .find(html)?.groupValues?.get(1)
            if (directM3u8 != null) {
                emitM3u8Link(sourceLabel, directM3u8.replace(slashEscaped, "/"), "$origin/", callback)
                return true
            }
        }
        if (loadExtractor(embedUrl, "$mainUrl/", subtitleCallback, callback)) {
            return true
        }
        val webViewResponse = runCatching {
            app.get(
                embedUrl,
                referer = "$mainUrl/",
                headers = embedHeaders,
                interceptor = WebViewResolver(
                    interceptUrl = Regex("""([.]m3u8|get[.]php|master[.]txt)""", RegexOption.IGNORE_CASE),
                    additionalUrls = listOf(Regex("""([.]m3u8)""", RegexOption.IGNORE_CASE)),
                    useOkhttp = false,
                    timeout = 15_000L
                )
            )
        }.getOrNull()
        val resolvedUrl = webViewResponse?.url?.takeIf {
            it.isNotBlank() && it != embedUrl && (it.contains(".m3u8") || it.contains("get.php"))
        }
        if (resolvedUrl != null) {
            emitM3u8Link(sourceLabel, resolvedUrl, "$origin/", callback)
            return true
        }
        return false
    }
    private suspend fun emitM3u8Link(
        sourceName: String,
        m3u8Url: String,
        referer: String,
        callback: (ExtractorLink) -> Unit
    ) {
        val headers = mapOf(
            "User-Agent" to USER_AGENT,
            "Referer" to referer
        )
        M3u8Helper.generateM3u8(
            source = sourceName,
            streamUrl = m3u8Url,
            referer = referer,
            headers = headers
        ).forEach(callback)
    }
    private fun NguonCFilmItem.toSearchResult(): SearchResponse? {
        val slugValue = slug?.takeIf { it.isNotBlank() } ?: return null
        val displayTitle = name?.takeIf { it.isNotBlank() }
            ?: originalName?.takeIf { it.isNotBlank() }
            ?: return null
        val image = resolveImage(thumbUrl, thumbUrlWebp, posterUrl, posterUrlWebp)
        val itemUrl = "$mainUrl/api/film/$slugValue"
        val isDubbed = language?.contains("Thuyết Minh", ignoreCase = true) == true ||
                language?.contains("Lồng Tiếng", ignoreCase = true) == true
        val isSubbed = language?.contains("Vietsub", ignoreCase = true) == true || !isDubbed
        val currentEpNum = parseEpisodeNumber(currentEpisode)
        return newAnimeSearchResponse(displayTitle, itemUrl, TvType.TvSeries) {
            this.posterUrl = image
            this.otherName = originalName
            this.quality = getQualityFromString(this@toSearchResult.quality)
            addDubStatus(
                isDub = isDubbed,
                isSub = isSubbed,
                episodes = currentEpNum
            )
        }
    }
    private fun extractSlugFromUrl(url: String): String {
        val clean = url.substringBefore("?").trimEnd('/')
        return clean.substringAfterLast('/')
    }
    private fun resolveImage(vararg candidates: String?): String? {
        val raw = candidates.firstOrNull { !it.isNullOrBlank() }?.trim() ?: return null
        return when {
            raw.startsWith("http://") || raw.startsWith("https://") -> raw
            raw.startsWith("//") -> "https:$raw"
            raw.startsWith("/") -> "$mainUrl$raw"
            else -> "$mainUrl/$raw"
        }
    }
    private fun isSeriesMovie(
        detail: NguonCMovieDetail,
        formatNames: List<String>,
        servers: List<NguonCEpisodeServer>
    ): Boolean {
        if (formatNames.any { it.contains("Phim lẻ", ignoreCase = true) }) return false
        if (formatNames.any {
                it.contains("Phim bộ", ignoreCase = true) ||
                        it.contains("TV Shows", ignoreCase = true)
            }
        ) return true
        if ((detail.totalEpisodes ?: 1) > 1) return true
        val maxItemsInServer = servers.maxOfOrNull { it.items?.size ?: 0 } ?: 0
        if (maxItemsInServer > 1) return true
        val epLabel = detail.currentEpisode.orEmpty()
        return epLabel.contains("Tập", ignoreCase = true) && !epLabel.contains("Full", ignoreCase = true)
    }
    private fun parseDurationMinutes(timeText: String?): Int? {
        if (timeText.isNullOrBlank()) return null
        val digits = timeText.takeWhile { it.isDigit() || it.isWhitespace() }.trim()
        return digits.toIntOrNull()
    }
    private fun parseEpisodeNumber(epText: String?): Int? {
        if (epText.isNullOrBlank()) return null
        if (epText.contains("Full", ignoreCase = true)) return null
        val numStr = StringBuilder()
        for (ch in epText) {
            if (ch.isDigit()) {
                numStr.append(ch)
            } else if (numStr.isNotEmpty()) {
                break
            }
        }
        return numStr.toString().toIntOrNull()
    }
    private fun parseShowStatus(currentEp: String?, totalEp: Int?): ShowStatus {
        val text = currentEp.orEmpty()
        if (text.contains("Hoàn tất", ignoreCase = true) || text.contains("Full", ignoreCase = true)) {
            return ShowStatus.Completed
        }
        val cur = parseEpisodeNumber(text)
        if (cur != null && totalEp != null && totalEp > 0 && cur >= totalEp) {
            return ShowStatus.Completed
        }
        return ShowStatus.Ongoing
    }
    private fun buildPlotWithOriginalName(
        originalName: String?,
        director: String?,
        cleanPlot: String?
    ): String {
        val parts = mutableListOf<String>()
        if (!originalName.isNullOrBlank()) {
            parts.add("Tên gốc: $originalName")
        }
        if (!director.isNullOrBlank()) {
            parts.add("Đạo diễn: $director")
        }
        if (!cleanPlot.isNullOrBlank()) {
            parts.add(cleanPlot)
        }
        return parts.joinToString(" | ")
    }
}
