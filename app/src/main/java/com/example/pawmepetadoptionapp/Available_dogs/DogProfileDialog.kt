import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pawmepetadoptionapp.Dog
import com.example.pawmepetadoptionapp.R


class DogProfileDialog(
    private val dog: Dog,
    private val onFosterSelected: (Dog) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.available_dialog_dog_profile, null)

        val imgDog = view.findViewById<ImageView>(R.id.imgDog)
        val txtName = view.findViewById<TextView>(R.id.txtDogName)
        val txtDetails = view.findViewById<TextView>(R.id.txtDogDetails)
        val btnChoose = view.findViewById<Button>(R.id.btnChooseFoster)

        imgDog.setImageResource(dog.imageResId)
        txtName.text = dog.name
        txtDetails.text = "${dog.breed} | ${dog.age} | ${dog.needs}"

        btnChoose.setOnClickListener {
            onFosterSelected(dog)
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
