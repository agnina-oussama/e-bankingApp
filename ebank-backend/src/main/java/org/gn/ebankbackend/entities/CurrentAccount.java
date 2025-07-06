package org.gn.ebankbackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("CA")
@Setter
@Getter
public class CurrentAccount extends BankAccount {
    private double overDraft;


}
