package tk.spotimatch.api.model.pairing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "pair")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pair {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long leftUserId;

    private Long rightUserId;

    private String leftUserName;

    private String rightUserName;

    @CreationTimestamp
    private Instant timestamp;

}
