package y.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class EmployeeSalesDTO implements Serializable {

    private String full_name;
    private Long count;

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeSalesDTO that = (EmployeeSalesDTO) o;
        return Objects.equals(full_name, that.full_name) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(full_name, count);
    }

    @Override
    public String toString() {
        return "EmployeeSalesDTO{" + "full_name='" + full_name + '\'' + ", count=" + count + '}';
    }
}
