package bou.amine.apps.mteo.api

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("currently") val currently: Datapoint?,
    @SerializedName("minutely") val minutely: Datablock?,
    @SerializedName("hourly") val hourly: Datablock?,
    @SerializedName("daily") val daily: Datablock?,
    @SerializedName("flags") val flags: Flags?,
    @SerializedName("alerts") val alerts: List<Alert>?,
    @SerializedName("offset") val offset: Int?
)


data class Flags(
    @SerializedName("sources") val sources: List<String>,
    @SerializedName("nearest-station") val nearestStation: Double,
    @SerializedName("units") val units: String,
    @SerializedName("darksky-unavailable") val darkskyUnavailable: String?
)

data class Datablock(
    @SerializedName("icon") val icon: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("data") val data: List<Datapoint>
)



data class Datapoint(
    @SerializedName("time") val time: Double,
    @SerializedName("summary") val summary: String?,
    @SerializedName("icon") val icon: String?,
    @SerializedName("apparentTemperature") val apparentTemperature: Double?, // hourly
    @SerializedName("apparentTemperatureHigh") val apparentTemperatureHigh: Double?, // daily
    @SerializedName("apparentTemperatureHighTime") val apparentTemperatureHighTime: Double?, // daily
    @SerializedName("apparentTemperatureLow") val apparentTemperatureLow: Double?, // daily
    @SerializedName("apparentTemperatureLowTime") val apparentTemperatureLowTime: Double?, // daily
    @SerializedName("apparentTemperatureMax") val apparentTemperatureMax: Double?, // daily
    @SerializedName("apparentTemperatureMaxTime") val apparentTemperatureMaxTime: Double?, // daily
    @SerializedName("apparentTemperatureMin") val apparentTemperatureMin: Double?, // daily
    @SerializedName("apparentTemperatureMinTime") val apparentTemperatureMinTime: Double?, // daily
    @SerializedName("cloudCover") val cloudCover: Double?,
    @SerializedName("dewPoint") val dewPoint: Double?,
    @SerializedName("humidity") val humidity: Double?,
    @SerializedName("moonPhase") val moonPhase: Double?, // daily
    @SerializedName("nearestStormBearing") val nearestStormBearing: Double?, // currently
    @SerializedName("nearestStormDistance") val nearestStormDistance: Double?, // currently
    @SerializedName("ozone") val ozone: Double?,
    @SerializedName("precipAccumulation") val precipAccumulation: Double?, // hourly + daily
    @SerializedName("precipIntensity") val precipIntensity: Double?,
    @SerializedName("precipIntensityError") val precipIntensityError: Double?,
    @SerializedName("precipIntensityMax") val precipIntensityMax: Double?, // daily
    @SerializedName("precipIntensityMaxTime") val precipIntensityMaxTime: Double?, // daily
    @SerializedName("precipProbability") val precipProbability: Double?,
    @SerializedName("precipType") val precipType: String?,
    @SerializedName("pressure") val pressure: String?,
    @SerializedName("sunriseTime") val sunriseTime: Double?, // daily
    @SerializedName("sunsetTime") val sunsetTime: Double?, // daily
    @SerializedName("temperature") val temperature: Double?, // hourly
    @SerializedName("temperatureHigh") val temperatureHigh: Double?, // daily
    @SerializedName("temperatureHighTime") val temperatureHighTime: Double?, // daily
    @SerializedName("temperatureLow") val temperatureLow: Double?, // daily
    @SerializedName("temperatureLowTime") val temperatureLowTime: Double?, // daily
    @SerializedName("temperatureMax") val temperatureMax: Double?, // daily
    @SerializedName("temperatureMaxTime") val temperatureMaxTime: Double?, // daily
    @SerializedName("temperatureMin") val temperatureMin: Double?, // daily
    @SerializedName("temperatureMinTime") val temperatureMinTime: Double?, // daily
    @SerializedName("uvIndex") val uvIndex: Int?,
    @SerializedName("uvIndexTime") val uvIndexTime: Double?, // daily
    @SerializedName("visibility") val visibility: Double?,
    @SerializedName("windBearing") val windBearing: Double?,
    @SerializedName("windGust") val windGust: Double?,
    @SerializedName("windGustTime") val windGustTime: Double?, // daily
    @SerializedName("windSpeed") val windSpeed: Double?
)

data class Alert(
    @SerializedName("description") val description: String,
    @SerializedName("expired") val expired: Double,
    @SerializedName("regions") val regions: List<String>,
    @SerializedName("severity") val severity: String,
    @SerializedName("time") val time: Double,
    @SerializedName("title") val title: String,
    @SerializedName("uri") val uri: String
)