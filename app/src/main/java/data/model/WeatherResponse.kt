package data.model

data class WeatherResponse(
    val coord: Coord,
    val weather: ArrayList<Weather>,
    val base: String,
    val main: Main,
    val visibility: Double,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Double,
    val sys: Sys,
    val timezone: Double,
    val id: Int,
    val name: String,
    val cod: Int,
)

data class  Coord (
    val lon: Double,
    val lat: Double
        )

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Double,
    val humidity: Double,
    val sea_level: Double,
    val grnd_level: Double,
)

data class Wind(
    val speed: Int,
    val deg: String,
    val gust: String
)

data class Clouds(
    val all: Int
)

data class Sys(
    val type: Double,
    val id: Int,
    val country: String,
    val sunrise: Double,
    val sunset: Double
)