
package codereading.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Question extends AbstractPersistable<Long>{

    private String title;
    
    @Column(columnDefinition="varchar(10000)")
    private String info;

    @Column(columnDefinition="varchar(10000)")
    private String code;
    
    boolean removed;

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isRemoved() {
        return removed;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public String getCode() {
        return code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    
}
