package ru.brinoauto.demo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : Activity() {
    private val prefs by lazy { getSharedPreferences("brino_demo", MODE_PRIVATE) }
    private lateinit var content: LinearLayout

    private val ink = Color.rgb(19, 30, 43)
    private val inkSoft = Color.rgb(35, 49, 64)
    private val muted = Color.rgb(111, 124, 137)
    private val accent = Color.rgb(0, 153, 165)
    private val accentDark = Color.rgb(0, 116, 128)
    private val paper = Color.rgb(244, 247, 249)
    private val line = Color.rgb(229, 235, 239)
    private val white = Color.WHITE

    private val services = listOf(
        "Замена масла в ДВС" to "от 900 ₽",
        "Замена масла в АКПП и вариаторе со сбросом счётчика старения масла" to "от 2 200 ₽",
        "Ремонт ходовой части" to "от 900 ₽",
        "Компьютерная диагностика" to "1 600 ₽",
        "Диагностика ходовой части" to "900 ₽",
        "Замена ГРМ" to "от 5 000 ₽",
        "Замена охлаждающей жидкости" to "от 2 000 ₽",
        "Замена сцепления" to "от 12 000 ₽",
        "Ремонт ДВС" to "от 15 000 ₽",
        "Ремонт МКПП" to "от 25 000 ₽",
        "Регулировка развал-схождения" to "от 2 000 ₽",
        "Шиномонтаж" to "от 2 500 ₽",
        "Промывка инжектора и дроссельной заслонки" to "от 2 000 ₽",
        "Ремонт рулевых реек" to "от 20 000 ₽",
        "Ремонт задней балки Peugeot 206" to "от 22 000 ₽",
        "Ремонт задней балки Peugeot Partner" to "от 25 000 ₽",
        "Ремонт задней балки Citroen Berlingo" to "от 25 000 ₽"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ink
        window.navigationBarColor = ink
        window.decorView.systemUiVisibility = 0
        showHome()
    }

    private fun rounded(color: Int, radius: Float, strokeColor: Int? = null): GradientDrawable =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius
            if (strokeColor != null) setStroke(dp(1), strokeColor)
        }

    private fun gradient(start: Int, end: Int, radius: Float): GradientDrawable =
        GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(start, end)).apply {
            cornerRadius = radius
        }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun text(
        value: String,
        size: Float,
        color: Int = ink,
        bold: Boolean = false
    ) = TextView(this).apply {
        text = value
        textSize = size
        setTextColor(color)
        if (bold) setTypeface(typeface, Typeface.BOLD)
        includeFontPadding = true
        setLineSpacing(dp(2).toFloat(), 1f)
    }

    private fun label(value: String, size: Float, color: Int = ink, bold: Boolean = false) =
        text(value, size, color, bold).apply { setPadding(0, dp(3), 0, dp(3)) }

    private fun card(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(18), dp(17), dp(18), dp(17))
        background = rounded(white, dp(20).toFloat(), line)
        elevation = dp(2).toFloat()
    }

    private fun gap(height: Int = 12) {
        content.addView(View(this), LinearLayout.LayoutParams(1, dp(height)))
    }

    private fun addCard(title: String, body: String? = null) {
        val box = card()
        box.addView(text(title, 16f, ink, true))
        if (body != null) {
            val description = text(body, 13f, muted)
            description.setPadding(0, dp(7), 0, 0)
            box.addView(description)
        }
        content.addView(box, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(12) })
    }

    private fun sectionTitle(title: String, subtitle: String? = null) {
        content.addView(text(title, 20f, ink, true).apply {
            setPadding(0, dp(8), 0, dp(2))
        })
        if (subtitle != null) content.addView(label(subtitle, 13f, muted))
        gap(10)
    }

    private fun pill(value: String, color: Int = accent) = TextView(this).apply {
        text = value
        textSize = 11f
        setTextColor(color)
        setTypeface(typeface, Typeface.BOLD)
        setPadding(dp(11), dp(7), dp(11), dp(7))
        background = rounded(Color.rgb(226, 245, 246), dp(30).toFloat())
    }

    private fun shell(title: String, active: String) {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(paper)
            layoutParams = ViewGroup.LayoutParams(-1, -1)
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(15), dp(22), dp(16))
            background = gradient(ink, inkSoft, 0f)
        }
        val brandRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        brandRow.addView(text("БРИНО-АВТО", 13f, white, true), LinearLayout.LayoutParams(0, -2, 1f))
        brandRow.addView(text("ОМСК  •  СТО", 10f, Color.rgb(161, 190, 199), true))
        header.addView(brandRow)
        header.addView(text(title, 25f, white, true).apply {
            setPadding(0, dp(11), 0, 0)
        })
        root.addView(header)

        val scroll = ScrollView(this).apply {
            clipToPadding = false
            isFillViewport = false
        }
        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(18), dp(18), dp(22))
        }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(8), dp(8), dp(8), dp(8))
            setBackgroundColor(white)
            elevation = dp(12).toFloat()
        }
        listOf("Главная" to "⌂", "Запись" to "+", "Мои записи" to "▤", "Профиль" to "◉").forEach { (item, symbol) ->
            val selected = item == active
            val cell = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(0, dp(5), 0, dp(5))
                background = if (selected) rounded(Color.rgb(229, 246, 247), dp(14).toFloat()) else rounded(white, dp(14).toFloat())
                setOnClickListener {
                    when (item) {
                        "Главная" -> showHome()
                        "Запись" -> showBooking()
                        "Мои записи" -> showBookings()
                        else -> showProfile()
                    }
                }
            }
            cell.addView(text(symbol, 19f, if (selected) accentDark else muted, true))
            cell.addView(text(item, 10f, if (selected) accentDark else muted, selected))
            nav.addView(cell, LinearLayout.LayoutParams(0, dp(52), 1f))
        }
        root.addView(nav)
        setContentView(root)
    }

    private fun action(value: String, primary: Boolean = true, click: () -> Unit) {
        val button = TextView(this).apply {
            text = value
            textSize = 14f
            setTypeface(typeface, Typeface.BOLD)
            gravity = Gravity.CENTER
            setTextColor(if (primary) white else ink)
            setPadding(dp(16), dp(15), dp(16), dp(15))
            background = if (primary) rounded(accent, dp(15).toFloat()) else rounded(white, dp(15).toFloat(), line)
            isClickable = true
            isFocusable = true
            setOnClickListener { click() }
        }
        content.addView(button, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(10) })
    }

    private fun openSite() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.brino-auto.ru")))
    }

    private fun showHome() {
        shell("Автосервис в Омске", "Главная")

        val hero = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(21), dp(20), dp(20))
            background = gradient(Color.rgb(19, 43, 58), Color.rgb(0, 111, 125), dp(24).toFloat())
        }
        hero.addView(pill("ОБСЛУЖИВАНИЕ И РЕМОНТ", Color.rgb(0, 117, 128)))
        hero.addView(text("Ваш автомобиль
в надёжных руках", 25f, white, true).apply {
            setPadding(0, dp(15), 0, dp(8))
        })
        hero.addView(text("Специализированный автосервис в Омске", 13f, Color.rgb(214, 230, 235)))
        val heroButton = TextView(this).apply {
            text = "Записаться в сервис  →"
            textSize = 14f
            setTypeface(typeface, Typeface.BOLD)
            gravity = Gravity.CENTER
            setTextColor(ink)
            setPadding(dp(16), dp(14), dp(16), dp(14))
            background = rounded(white, dp(14).toFloat())
            setOnClickListener { showBooking() }
        }
        hero.addView(heroButton, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(19) })
        content.addView(hero)
        gap(20)

        sectionTitle("Сервис для вашего авто", "Работаем с марками, указанными на сайте")
        val brands = "Peugeot  •  Citroen  •  Renault\nNissan  •  Infiniti  •  Mitsubishi\nHyundai  •  KIA"
        val brandsCard = card()
        brandsCard.addView(text(brands, 14f, ink, true))
        content.addView(brandsCard)
        gap(18)

        val promo = card()
        promo.addView(pill("АКЦИЯ"))
        promo.addView(text("Диагностический осмотр бесплатно", 18f, ink, true).apply {
            setPadding(0, dp(12), 0, dp(5))
        })
        promo.addView(text("При покупке и замене масла и масляного фильтра", 13f, muted))
        content.addView(promo)
        gap(18)

        sectionTitle("Чем помочь вашему авто?")
        action("Услуги и цены  →", false) { showServices() }
        action("Записаться в сервис  →") { showBooking() }
        action("Позвонить  ·  +7 (3812) 59-49-39", false) {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:+73812594939")))
        }

        sectionTitle("Контакты")
        val contacts = card()
        contacts.addView(text("Адрес", 11f, muted, true))
        contacts.addView(label("Омск, ул. 1-я Железнодорожная, 1 к.1", 14f, ink, true))
        contacts.addView(label("Телефон: +7 (3812) 59-49-39", 13f, ink))
        contacts.addView(label("Email: info@brino-auto.ru", 13f, ink))
        content.addView(contacts)
        gap(12)
        action("Открыть сайт  ↗", false) { openSite() }
        content.addView(text("Демо-приложение. Записи сохраняются только на этом устройстве и не отправляются в автосервис.", 11f, muted))
    }

    private fun showServices() {
        shell("Услуги и цены", "")
        content.addView(text("Подтверждённые услуги автосервиса", 14f, muted).apply {
            setPadding(0, 0, 0, dp(12))
        })
        services.forEach { (name, price) ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(dp(16), dp(15), dp(16), dp(15))
                background = rounded(white, dp(17).toFloat(), line)
            }
            row.addView(text(name, 14f, ink, true), LinearLayout.LayoutParams(0, -2, 1f))
            row.addView(text(price, 13f, accentDark, true).apply {
                gravity = Gravity.END
                setPadding(dp(8), 0, 0, 0)
            })
            content.addView(row, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(9) })
        }
        content.addView(text("Цены указаны «от» там, где так указано на сайте. Итоговую стоимость уточняйте в сервисе. Здесь приведены только 17 подтверждённых позиций.", 11f, muted).apply {
            setPadding(0, dp(8), 0, dp(8))
        })
        action("Записаться на услугу") { showBooking() }
    }

    private fun field(hint: String, value: String = ""): EditText {
        val input = EditText(this).apply {
            this.hint = hint
            setText(value)
            textSize = 14f
            setTextColor(ink)
            setHintTextColor(Color.rgb(151, 162, 171))
            setSingleLine(true)
            setPadding(dp(15), dp(12), dp(15), dp(12))
            background = rounded(white, dp(14).toFloat(), line)
        }
        content.addView(input, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(11) })
        return input
    }

    private fun showProfile() {
        shell("Карточка клиента", "Профиль")
        content.addView(pill("ДЕМО-ПРОФИЛЬ"))
        content.addView(text("Ваши данные", 20f, ink, true).apply {
            setPadding(0, dp(13), 0, dp(5))
        })
        content.addView(text("Заполните карточку — данные останутся на этом устройстве.", 13f, muted))
        gap(12)
        val name = field("Ваше имя", prefs.getString("name", "") ?: "")
        val phone = field("Телефон", prefs.getString("phone", "") ?: "")
        sectionTitle("Ваш автомобиль")
        val brand = field("Марка", prefs.getString("brand", "") ?: "")
        val model = field("Модель", prefs.getString("model", "") ?: "")
        val year = field("Год выпуска", prefs.getString("year", "") ?: "")
        val plate = field("Госномер (необязательно)", prefs.getString("plate", "") ?: "")
        action("Сохранить карточку") {
            prefs.edit().putString("name", name.text.toString().trim())
                .putString("phone", phone.text.toString().trim())
                .putString("brand", brand.text.toString().trim())
                .putString("model", model.text.toString().trim())
                .putString("year", year.text.toString().trim())
                .putString("plate", plate.text.toString().trim()).apply()
            Toast.makeText(this, "Карточка сохранена на устройстве", Toast.LENGTH_SHORT).show()
        }
        content.addView(text("Данные вводятся пользователем и хранятся локально. Это не учётная запись на сервере.", 11f, muted))
    }

    private fun showBooking() {
        shell("Запись в сервис", "Запись")
        content.addView(pill("ЛОКАЛЬНАЯ ДЕМО-ЗАПИСЬ"))
        content.addView(text("Оставьте данные о визите", 20f, ink, true).apply {
            setPadding(0, dp(13), 0, dp(5))
        })
        content.addView(text("Форма сохранит заявку в приложении. Она не будет отправлена в автосервис.", 13f, muted))
        gap(14)
        val name = field("Ваше имя", prefs.getString("name", "") ?: "")
        val phone = field("Телефон", prefs.getString("phone", "") ?: "")
        val car = field("Автомобиль: марка и модель")
        val service = field("Какая услуга нужна?")
        val date = field("Желаемая дата и время")
        action("Сохранить демо-запись") {
            val n = name.text.toString().trim()
            val p = phone.text.toString().trim()
            val s = service.text.toString().trim()
            if (!BookingValidator.isValid(n, p, s)) {
                Toast.makeText(this, "Укажите имя, телефон (не менее 10 цифр) и услугу", Toast.LENGTH_LONG).show()
                return@action
            }
            prefs.edit().putString("name", n).putString("phone", p).apply()
            val item = JSONObject().put("name", n).put("phone", p)
                .put("car", car.text.toString().trim()).put("service", s)
                .put("date", date.text.toString().trim()).put("status", "Демо-запись")
            val all = readBookings()
            all.put(item)
            prefs.edit().putString("bookings", all.toString()).apply()
            Toast.makeText(this, "Демо-запись сохранена", Toast.LENGTH_SHORT).show()
            showBookings()
        }
    }

    private fun readBookings(): JSONArray = try {
        JSONArray(prefs.getString("bookings", "[]"))
    } catch (_: Exception) {
        JSONArray()
    }

    private fun showBookings() {
        shell("Мои записи", "Мои записи")
        val all = readBookings()
        if (all.length() == 0) {
            val empty = card()
            empty.addView(text("Пока нет записей", 18f, ink, true))
            empty.addView(label("Создайте демо-запись — она появится здесь.", 13f, muted))
            content.addView(empty)
        } else {
            for (i in all.length() - 1 downTo 0) {
                val item = all.optJSONObject(i) ?: continue
                val details = "Автомобиль: " + item.optString("car").ifBlank { "не указан" } +
                    "\nДата: " + item.optString("date").ifBlank { "не указана" } +
                    "\nКонтакт: " + item.optString("name") + " · " + item.optString("phone") +
                    "\nСтатус: " + item.optString("status")
                val box = card()
                box.addView(text(item.optString("service"), 15f, ink, true))
                box.addView(label(details, 12f, muted))
                content.addView(box, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(11) })
            }
        }
        action("Новая запись") { showBooking() }
        content.addView(text("История хранится только на этом телефоне. Это не подтверждение визита в СТО.", 11f, muted))
    }
}
