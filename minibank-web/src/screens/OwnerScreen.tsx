import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getAccounts, getBalance, getOwnerFromOwnerId } from "../api";
import type { VisibleAccountDetails, Account, Owner } from "../api";

export default function OwnerScreen() {
  const { ownerId } = useParams<{ ownerId: string }>(); // from /owner/:ownerId
  const [owner, setOwner] = useState<Owner | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [accounts, setAccounts] = useState<Account[] | null>(null);
  const [visibleAccountsDetails, setVisibleAccountDetails] = useState<VisibleAccountDetails[] | null>(null);

  useEffect(() => {
    if (!ownerId) {
      setError("Missing owner id in URL");
      setLoading(false);
      return;
    }

    let isMounted = true;

    async function fetchData() {
      setLoading(true);
      setError(null);
      try {
        const ownerIdNum = Number(ownerId);
        const [ownerData, accountData] = await Promise.all([
          getOwnerFromOwnerId(ownerIdNum),
          getAccounts(ownerIdNum)
        ]);

        if (!isMounted) {
          return;
        }

        setOwner(ownerData);
        setAccounts(accountData);

        const details: VisibleAccountDetails[] = [];
        for (const acc of accountData) {
          try {
            const balance = await getBalance(acc.id);
            details.push({
              iban: acc.iban,
              balance,
              createdAt: acc.createdAt
            });
          } catch (err: any) {
            setError(err.message ?? "Failed to load account balance");
          }
        }

        if (!isMounted) {
          return;
        }

        setVisibleAccountDetails(details);
      } catch (err: any) {
        if (isMounted) {
          setError(err.message ?? "Failed to load owner");
        }
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    }

    fetchData();
    
    return () => {
      isMounted = false;
    };
  }, [ownerId]);

  if (loading) return <p>Loading owner info...</p>;
  if (error) return <p>Error: {error}</p>;
  if (!owner) return <p>No owner found.</p>;

  return (
    <div
      style={{
        position: "fixed",
        top: "20px",
        left: "20px"
      }}>
      <h2 className="text-xl font-semibold">
        {owner.firstname} {owner.surname}
      </h2>
      <p><strong>Email:</strong> {owner.email}</p>
      <p><strong>Phone:</strong> {owner.phonenumber}</p>

      <section style={{ marginTop: "20px" }}>
        <p><strong>Accounts:</strong></p>
        {visibleAccountsDetails && visibleAccountsDetails.length > 0 ? (
          <ul style={{ listStyle: "none", padding: 0 }}>
            {visibleAccountsDetails.map((acc) => (
              <li
                key={acc.iban}
                style={{
                  marginBottom: "12px",
                  border: "1px solid #ddd",
                  borderRadius: "4px",
                  padding: "12px",
                  maxWidth: "320px"
                }}>
                <p><strong>IBAN:</strong> {acc.iban}</p>
                <p><strong>Balance:</strong> {acc.balance}</p>
                <p><strong>Created:</strong> {new Date(acc.createdAt).toLocaleString()}</p>
              </li>
            ))}
          </ul>
        ) : (
          <p>No accounts yet.</p>
        )}
      </section>
    </div>
  );
}
