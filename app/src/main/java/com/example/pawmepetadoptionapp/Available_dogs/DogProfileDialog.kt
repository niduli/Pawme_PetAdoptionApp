import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pawmepetadoptionapp.Available_dogs.FosterDogs
import com.example.pawmepetadoptionapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DogProfileDialog(
    private val dog: FosterDogs,
    private val onFosterSelected: (FosterDogs) -> Unit
) : DialogFragment() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.available_dialog_dog_profile, null)

        val imgDog = view.findViewById<ImageView>(R.id.imgDog)
        val txtName = view.findViewById<TextView>(R.id.txtDogName)
        val txtDetails = view.findViewById<TextView>(R.id.txtDogDetails)
        val btnChoose = view.findViewById<Button>(R.id.btnChooseFoster)

        val resId = resources.getIdentifier(dog.imageName, "drawable", requireContext().packageName)
        imgDog.setImageResource(resId)
        txtName.text = dog.name
        txtDetails.text = "${dog.breed} | ${dog.age} | ${dog.needs}"

        btnChoose.setOnClickListener {
            chooseFostering(dog)
        }

        builder.setView(view)
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun chooseFostering(dog: FosterDogs) {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = user.uid
        val dogId = dog.id


        // Get current date as foster start
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fosterStart = Date()
        val fosterStartStr = dateFormat.format(fosterStart)

        // Parse duration field (e.g., "2 weeks", "5 days")
        val durationParts = dog.duration.split(" ")
        val number = durationParts[0].toIntOrNull() ?: 0
        val unit = durationParts[1].lowercase()

        // Calculate foster end date
        val calendar = Calendar.getInstance()
        calendar.time = fosterStart
        when (unit) {
            "day", "days" -> calendar.add(Calendar.DAY_OF_YEAR, number)
            "week", "weeks" -> calendar.add(Calendar.WEEK_OF_YEAR, number)
            "month", "months" -> calendar.add(Calendar.MONTH, number)
            else -> calendar.add(Calendar.DAY_OF_YEAR, 7) // default 7 days
        }
        val fosterEndStr = dateFormat.format(calendar.time)

        // Save fostering data under user's Firestore record
        val fosterData = hashMapOf(
            "fosterStartDate" to fosterStartStr,
            "fosterEndDate" to fosterEndStr,
            "name" to dog.name,
            "breed" to dog.breed,
            "age" to dog.age,
            "needs" to dog.needs,
            "imageResName" to dog.imageName
        )

        // Step 1: Update dog's availability
        db.collection("dogs").document(dogId)
            .update("available", false)
            .addOnSuccessListener {
                // Step 2: Save to user's fostered dogs
                db.collection("users").document(userId)
                    .collection("currentFosters").document(dogId)
                    .set(fosterData)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Fostering confirmed!", Toast.LENGTH_SHORT).show()
                        onFosterSelected(dog)  // callback for UI
                        dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to assign dog to user", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update dog status", Toast.LENGTH_SHORT).show()
            }
    }
}
