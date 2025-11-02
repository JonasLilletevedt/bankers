import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getOwnerFromOwnerId } from "../api";
import type { Owner } from "../api";

export default function OwnerScreen() {
  const { ownerId } = useParams<{ ownerId: string }>(); // from /owner/:ownerId
  const [owner, setOwner] = useState<Owner | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // if URL didn't have an id
    if (!ownerId) {
      setError("Missing owner id in URL");
      setLoading(false);
      return;
    }

    async function fetchOwner() {
      try {
        const data = await getOwnerFromOwnerId(Number(ownerId));
        setOwner(data);
      } catch (err: any) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }

    fetchOwner();
  }, [ownerId]);

  if (loading) return <p>Loading owner info...</p>;
  if (error) return <p>Error: {error}</p>;
  if (!owner) return <p>No owner found.</p>;

  return (
    <div className="p-4">
      <h2 className="text-xl font-semibold">
        {owner.firstname} {owner.surname}
      </h2>
      <p><strong>Email:</strong> {owner.email}</p>
      <p><strong>Phone:</strong> {owner.phonenumber}</p>
    </div>
  );
}
