public interface Taskable {

    //int getId();
    String getName();
    void setName(String name);
    String getDescription();
    TaskStatus getStatus();
    void setStatus(TaskStatus status);

}
