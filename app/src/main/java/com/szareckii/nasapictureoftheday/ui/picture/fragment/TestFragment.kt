package com.szareckii.nasapictureoftheday.ui.picture.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.szareckii.nasapictureoftheday.R
import com.szareckii.nasapictureoftheday.ui.picture.recyclerView.*
import kotlinx.android.synthetic.main.recycler_test.*

class TestFragment: Fragment() {

    lateinit var itemTouchHelper: ItemTouchHelper
    private var isNewList = false
    private lateinit var adapter: RecyclerActivityAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recycler_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arrayListOf(
            Pair(Data(1,"Mars", ""), false)
        )

        data.add(0, Pair(Data(1,"Header"), false))

        adapter = RecyclerActivityAdapter(
                object : OnListItemClickListenerTest {
                    override fun onItemClick(data: Data) {
                        toast(data.someText)
                    }
                },
                data,
                object : OnStartDragListenerTest {
                    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                        itemTouchHelper.startDrag(viewHolder)
                    }
                }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerActivityFAB.setOnClickListener { adapter.appendItem() }
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallbackTest(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerActivityDiffUtilFAB.setOnClickListener { changeAdapterData() }
    }


    private fun changeAdapterData() {
        adapter.setItems(createItemList(isNewList).map { it })
        isNewList = !isNewList
    }

    private fun createItemList(instanceNumber: Boolean): List<Pair<Data, Boolean>> {
        return when (instanceNumber) {
            false -> listOf(
                Pair(Data(0, "Header"), false),
                Pair(Data(1, "Mars", ""), false),
                Pair(Data(2, "Mars", ""), false),
                Pair(Data(3, "Mars", ""), false),
                Pair(Data(4, "Mars", ""), false),
                Pair(Data(5, "Mars", ""), false),
                Pair(Data(6, "Mars", ""), false)
            )
            true -> listOf(
                Pair(Data(0, "Header"), false),
                Pair(Data(1, "Mars", ""), false),
                Pair(Data(2, "Jupiter", ""), false),
                Pair(Data(3, "Mars", ""), false),
                Pair(Data(4, "Neptune", ""), false),
                Pair(Data(5, "Saturn", ""), false),
                Pair(Data(6, "Mars", ""), false)
            )
        }
    }


    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }
}









//    private var show = false
//
//    override fun onCreateView(
//            inflater: LayoutInflater, container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.activity_animations_bonus_start, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        backgroundImage.setOnClickListener { if (show) hideComponents() else showComponents() }
//    }
//
//    private fun showComponents() {
//        show = true
//
//        val constraintSet = ConstraintSet()
//        constraintSet.clone(activity, R.layout.activity_animations_bonus_end)
//
//        val transition = ChangeBounds()
//        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
//        transition.duration = 1200
//
//        TransitionManager.beginDelayedTransition(constraint_container, transition)
//        constraintSet.applyTo(constraint_container)
//    }
//
//    private fun hideComponents() {
//        show = false
//
//        val constraintSet = ConstraintSet()
//        constraintSet.clone(activity, R.layout.activity_animations_bonus_start)
//
//        val transition = ChangeBounds()
//        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
//        transition.duration = 1200
//
//        TransitionManager.beginDelayedTransition(constraint_container, transition)
//        constraintSet.applyTo(constraint_container)
//    }
//}
