package ru.brinoauto.demo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
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
    private val ink = Color.rgb(27, 38, 50)
    private val muted = Color.rgb(105, 117, 128)
    private val accent = Color.rgb(20, 112, 132)
    private val paper = Color.rgb(246, 248, 249)

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
        showHome()
    }

    private fun shell(title: String) {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(paper)
            layoutParams = ViewGroup.LayoutParams(-1, -1)
        }
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(22, 18, 22, 16)
            setBackgroundColor(ink)
            addView(label("БРИНО-АВТО", 12, Color.rgb(126, 207, 220), true))
            addView(label(title, 25, Color.WHITE, true))
        }
        root.addView(header)
        val scroll = ScrollView(this)
        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 24)
        }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        val nav = LinearLayout(this).apply {
            setBackgroundColor(Color.WHITE)
            setPadding(4, 6, 4, 6)
            gravity = Gravity.CENTER
        }
        listOf("Главная", "Запись", "Мои записи", "Профиль").forEach { item ->
            nav.addView(Button(this).apply {
                text = item
                textSize = 10f
                isAllCaps = false
                setTextColor(accent)
                setOnClickListener {
                    when (item) {
                        "Главная" -> showHome()
                        "Запись" -> showBooking()
                        "Мои записи" -> showBookings()
                        else -> showProfile()
                    }
                }
            }, LinearLayout.LayoutParams(0, 48, 1f))
        }
        root.addView(nav)
        setContentView(root)
    }

    private fun label(text: String, size: Int, color: Int = ink, bold: Boolean = false) =
        TextView(this).apply {
            this.text = text
            textSize = size.toFloat()
            setTextColor(color)
            if (bold) setTypeface(typeface, Typeface.BOLD)
            setPadding(0, 4, 0, 4)
        }

    private fun card() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(16, 14, 16, 14)
        setBackgroundColor(Color.WHITE)
        elevation = 2f
    }

    private fun addCard(title: String, body: String? = null) {
        val box = card()
        box.addView(label(title, 17, ink, true))
        if (body != null) box.addView(label(body, 14, muted))
        content.addView(box, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = 12 })
    }

    private fun action(text: String, click: () -> Unit) {
        content.addView(Button(this).apply {
            this.text = text
            isAllCaps = false
            setTextColor(Color.WHITE)
            setBackgroundColor(accent)
            setOnClickListener { click() }
        }, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = 10 })
    }

    private fun showHome() {
        shell("Автосервис в Омске")
        addCard("Ремонт и обслуживание автомобилей", "Специализация: Peugeot, Citroen, Renault, Nissan, Infiniti, Mitsubishi, Hyundai и KIA.")
        addCard("Акция", "Бесплатный диагностический осмотр автомобиля при покупке и замене масла и масляного фильтра.")
        addCard("Контакты", "1-я Железнодорожная, 1 к.1, Омск\n+7 (3812) 59-49-39\ninfo@brino-auto.ru")
        action("Записаться в сервис") { showBooking() }
        action("Услуги и цены") { showServices() }
        action("Позвонить в сервис") { startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:+73812594939"))) }
        action("Открыть сайт") { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.brino-auto.ru"))) }
        content.addView(label("Демо-приложение. Записи сохраняются только на этом устройстве и не отправляются в автосервис.", 12, muted))
    }

    private fun showServices() {
        shell("Услуги и цены")
        content.addView(label("Цены указаны «от» там, где так указано на сайте. Итоговую стоимость уточняйте в сервисе.", 13, muted))
        services.forEach { (name, price) ->
            val row = card()
            row.addView(label(name, 15, ink, true))
            row.addView(label(price, 14, accent, true))
            content.addView(row, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = 8 })
        }
        content.addView(label("На сайте также указаны дополнительные услуги. Здесь приведены только подтверждённые позиции.", 12, muted))
    }

    private fun field(hint: String, value: String = ""): EditText {
        val input = EditText(this).apply {
            this.hint = hint
            setText(value)
            textSize = 15f
            setSingleLine(true)
            setPadding(12, 8, 12, 8)
            setBackgroundColor(Color.WHITE)
        }
        content.addView(input, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = 9 })
        return input
    }

    private fun showProfile() {
        shell("Карточка клиента")
        content.addView(label("Демо-профиль", 13, accent, true))
        val name = field("Ваше имя", prefs.getString("name", "") ?: "")
        val phone = field("Телефон", prefs.getString("phone", "") ?: "")
        content.addView(label("Автомобиль", 17, ink, true))
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
        content.addView(label("Данные вводятся пользователем и хранятся локально. Это не учётная запись на сервере.", 12, muted))
    }

    private fun showBooking() {
        shell("Запись в сервис")
        content.addView(label("Демо-запись останется в приложении и не будет отправлена в сервис.", 13, muted))
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
    } catch (_: Exception) { JSONArray() }

    private fun showBookings() {
        shell("Мои записи")
        val all = readBookings()
        if (all.length() == 0) {
            addCard("Пока нет записей", "Создайте демо-запись — она появится здесь.")
        } else {
            for (i in all.length() - 1 downTo 0) {
                val item = all.optJSONObject(i) ?: continue
                val details = "Автомобиль: " + item.optString("car").ifBlank { "не указан" } +
                    "\nДата: " + item.optString("date").ifBlank { "не указана" } +
                    "\nКонтакт: " + item.optString("name") + " · " + item.optString("phone") +
                    "\nСтатус: " + item.optString("status")
                addCard(item.optString("service"), details)
            }
        }
        action("Новая запись") { showBooking() }
        content.addView(label("История хранится только на этом телефоне. Это не подтверждение визита в СТО.", 12, muted))
    }
}
