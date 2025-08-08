package com.example.pawmepetadoptionapp

object AssignedTasksStore {
    private val assignedTasks = mutableListOf<AssignedTask>()

    fun addTask(task: AssignedTask) {
        assignedTasks.add(task)
    }

    fun getTasks(): List<AssignedTask> = assignedTasks

    fun updateTask(index: Int, task: AssignedTask) {
        if (index in assignedTasks.indices) {
            assignedTasks[index] = task
        }
    }

    fun removeTask(task: AssignedTask) {
        assignedTasks.remove(task)
    }

    fun clearTasks() {
        assignedTasks.clear()
    }
}