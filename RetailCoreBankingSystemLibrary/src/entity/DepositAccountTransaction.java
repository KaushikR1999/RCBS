/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import util.enumeration.TransactionStatus;
import util.enumeration.TransactionType;

/**
 *
 * @author kaushikr
 */
@Entity
public class DepositAccountTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long depositAccountTransactionId;
    
    @Column(nullable = false)
    private Date transactionDateTime;
    
    @Column(nullable = false)
    private TransactionType type;
    
    @Column(nullable = false, length = 64)
    private String code;
    
    @Column(nullable = false, length = 64)
    private String reference;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private TransactionStatus status;
    
    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private DepositAccountTransaction sourceTransaction;
    
    @OneToOne (fetch = FetchType.LAZY, mappedBy = "sourceTransaction")
    private DepositAccountTransaction destinationTransaction;
    
    @ManyToOne (fetch = FetchType.LAZY, optional = true)
    @JoinColumn(nullable = false)
    private DepositAccount depositAccount;


    public Long getDepositAccountTransactionId() {
        return depositAccountTransactionId;
    }

    public void setDepositAccountTransactionId(Long depositAccountTransactionId) {
        this.depositAccountTransactionId = depositAccountTransactionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (depositAccountTransactionId != null ? depositAccountTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the depositAccountTransactionId fields are not set
        if (!(object instanceof DepositAccountTransaction)) {
            return false;
        }
        DepositAccountTransaction other = (DepositAccountTransaction) object;
        if ((this.depositAccountTransactionId == null && other.depositAccountTransactionId != null) || (this.depositAccountTransactionId != null && !this.depositAccountTransactionId.equals(other.depositAccountTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DepositAccountTransaction[ id=" + depositAccountTransactionId + " ]";
    }

    /**
     * @return the transactionDateTime
     */
    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    /**
     * @param transactionDateTime the transactionDateTime to set
     */
    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    /**
     * @return the type
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(TransactionType type) {
        this.type = type;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the status
     */
    public TransactionStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
    
}
