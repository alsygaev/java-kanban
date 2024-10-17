1. Убрать. Все SOUT перенести в Main
2. По ТЗ в методе по созданию задачи должен быть только один параметр - сам объект.
3. После добавления Subtask у Epic может меняться статус. Добавить обновление статуса
4. Изменил метод getAllTasks() -> return new ArrayList<>(tasks.values());
5. Переименовал getAllEpicById(int id) в getEpicById(int id)
6. Добавить обновление статуса Epic при действиях с Subtask
7. Добавить метод deleteAllEpics()
   