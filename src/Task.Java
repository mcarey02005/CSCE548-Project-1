public class Task {
    public Integer taskId;
    public Integer userId;
    public Integer categoryId;
    public String description;
    public boolean completed;

    public Task() {}
    public Task(Integer tId, Integer uId, Integer cId, String desc, boolean done) {
        this.taskId = tId; this.userId = uId; this.categoryId = cId; this.description = desc; this.completed = done;
    }
    @Override public String toString() {
        return taskId + ": user=" + userId + ", cat=" + categoryId + ", desc=\"" + description + "\", done=" + completed;
    }
}
