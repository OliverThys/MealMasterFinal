let ingredientIndex = 1;

document.addEventListener("DOMContentLoaded", () => {
    // 🎯 Réinitialisation recherche
    const resetBtn = document.getElementById("reset-search");
    const input = document.getElementById("search-input");
    const form = document.getElementById("search-form");

    if (resetBtn && input && form) {
        resetBtn.addEventListener("click", () => {
            input.value = "";
            form.submit();
        });
    }

    // 🎯 Drag des noms de recette
    const recipeNames = document.querySelectorAll(".recipe-name");

    recipeNames.forEach(name => {
        name.setAttribute("draggable", "true");

        name.addEventListener("dragstart", (e) => {
            e.dataTransfer.setData("text/plain", e.target.textContent.trim());
            e.dataTransfer.effectAllowed = "move";
            e.target.classList.add("dragging");
        });

        name.addEventListener("dragend", (e) => {
            e.target.classList.remove("dragging");
        });
    });

    // 🎯 Drop zones (MIDI & SOIR)
    const dropzones = document.querySelectorAll(".calendar-slot");

    dropzones.forEach(slot => {
        slot.addEventListener("dragover", (e) => {
            e.preventDefault();
            e.dataTransfer.dropEffect = "move";
            slot.classList.add("droppable-hover");
        });

        slot.addEventListener("dragleave", () => {
            slot.classList.remove("droppable-hover");
        });

        slot.addEventListener("drop", (e) => {
            e.preventDefault();
            slot.classList.remove("droppable-hover");

            const recipeName = e.dataTransfer.getData("text/plain");
            if (!recipeName) return;

            slot.innerHTML = slot.classList.contains("midi") ? "Midi" : "Soir";

            const droppedItem = document.createElement("div");
            droppedItem.textContent = recipeName;
            droppedItem.classList.add("dropped-recipe");
            slot.appendChild(droppedItem);
        });
    });

    // 💾 Save Planning
    const saveBtn = document.getElementById("save-planning-btn");
    if (saveBtn) {
        saveBtn.addEventListener("click", () => {
            const slots = document.querySelectorAll(".calendar-slot");
            const planningData = [];

            slots.forEach(slot => {
                const caseId = parseInt(slot.dataset.caseId);
                const recipeDiv = slot.querySelector(".dropped-recipe");

                if (recipeDiv) {
                    const recipeName = recipeDiv.textContent.trim();
                    planningData.push({ caseId, recipeName });
                }
            });

            fetch("/calendar/save", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(planningData)
            })
            .then(response => response.text())
            .then(msg => alert("✅ Planning saved!"))
            .catch(err => {
                console.error("Erreur:", err);
                alert("❌ Failed to save planning");
            });
        });
    }

    // 🔁 Chargement du planning
    fetch("/calendar/load")
        .then(response => response.json())
        .then(data => {
            data.forEach(slot => {
                const targetBox = document.querySelector(`[data-case-id="${slot.caseId}"]`);
                if (targetBox) {
                    const recipeDiv = document.createElement("div");
                    recipeDiv.className = "dropped-recipe";
                    recipeDiv.textContent = slot.recipeName;
                    targetBox.appendChild(recipeDiv);
                }
            });
        })
        .catch(error => console.error("❌ Erreur de chargement planning:", error));

    // ❌ Reset Planning
    const resetBtnPlanning = document.getElementById("reset-planning-btn");
    if (resetBtnPlanning) {
        resetBtnPlanning.addEventListener("click", () => {
            if (!confirm("❗Êtes-vous sûr de vouloir réinitialiser votre planning ?")) return;

            fetch("/calendar/reset", {
                method: "POST",
                headers: { "Content-Type": "application/json" }
            })
            .then(response => response.text())
            .then(msg => {
                alert("🗑️ Planning reset!");
                document.querySelectorAll(".calendar-slot").forEach(slot => {
                    slot.innerHTML = slot.classList.contains("midi") ? "Midi" : "Soir";
                });
            })
            .catch(err => {
                console.error("Erreur:", err);
                alert("❌ Erreur lors de la réinitialisation");
            });
        });
    }
        // ➕ Ajout dynamique d'ingrédients (page /recipes/add)
        const addButton = document.getElementById("add-ingredient-btn");

        if (addButton) {
            addButton.addEventListener("click", () => {
                const container = document.getElementById("ingredients-container");
        
                // 🔍 Récupération des valeurs du dernier ingrédient (index courant - 1)
                const lastIndex = ingredientIndex - 1;
                const lastName = document.querySelector(`[name="ingredients[${lastIndex}].name"]`);
                const lastQty = document.querySelector(`[name="ingredients[${lastIndex}].quantity"]`);
                const lastUnit = document.querySelector(`[name="ingredients[${lastIndex}].unit"]`);
        
                // 🧾 Ajout au preview si valeur non vide
                if (lastName && lastQty && lastUnit && lastName.value && lastQty.value) {
                    let preview = document.getElementById("ingredients-preview");
                    if (!preview) {
                        preview = document.createElement("div");
                        preview.id = "ingredients-preview";
                        container.parentElement.insertBefore(preview, addButton);
                    }
        
                    const line = document.createElement("div");
                    line.classList.add("ingredient-preview-line");
                    line.innerHTML = `
                        <span>${lastQty.value} ${lastUnit.value} of ${lastName.value}</span>
                        <button type="button" class="remove-ingredient-btn">🗑️</button>
                    `;
                    preview.appendChild(line);
        
                    // 🗑️ Suppression au clic sur le bouton
                    const removeBtn = line.querySelector(".remove-ingredient-btn");
                    removeBtn.addEventListener("click", () => {
                        line.remove();
                    });
        
                    // 🚫 Effacer la ligne précédente
                    lastName.parentElement.remove();
                }
        
                // ➕ Nouvelle ligne de formulaire
                const row = document.createElement("div");
                row.classList.add("ingredient-row");
                row.innerHTML = `
                    <input type="text" name="ingredients[${ingredientIndex}].name" placeholder="Ingredient name" required />
                    <input type="number" name="ingredients[${ingredientIndex}].quantity" placeholder="Quantity" min="0" required />
                    <select name="ingredients[${ingredientIndex}].unit">
                        <option value="pieces">pieces</option>
                        <option value="grams">grams</option>
                        <option value="ml">ml</option>
                        <option value="tbsp">tbsp</option>
                        <option value="tsp">tsp</option>
                    </select>
                `;
                container.appendChild(row);
                ingredientIndex++;
            });
        }
        
        if (form) {
            form.addEventListener("submit", () => {
                const lastRow = document.querySelector(".ingredient-row:last-child");
                const nameInput = lastRow?.querySelector("input[name*='.name']");
                const qtyInput = lastRow?.querySelector("input[name*='.quantity']");
        
                // Supprime la ligne si elle est vide
                if (nameInput && qtyInput && !nameInput.value.trim() && !qtyInput.value.trim()) {
                    lastRow.remove();
                }
            });
        }
        const generateBtn = document.getElementById("generate-shopping-list-btn");

if (generateBtn) {
    generateBtn.addEventListener("click", () => {
        // Étape 1 : sauvegarder automatiquement le planning
        const slots = document.querySelectorAll(".calendar-slot");
        const planningData = [];

        slots.forEach(slot => {
            const caseId = parseInt(slot.dataset.caseId);
            const recipeDiv = slot.querySelector(".dropped-recipe");

            if (recipeDiv) {
                const recipeName = recipeDiv.textContent.trim();
                planningData.push({ caseId, recipeName });
            }
        });

        fetch("/calendar/save", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(planningData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Erreur lors de la sauvegarde du planning");
            }
            return response.text();
        })
        .then(() => {
            // Étape 2 : une fois le planning sauvegardé, on peut générer la liste de courses
            return fetch("/calendar/shopping-list");
        })
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById("shopping-list-content");
            container.innerHTML = "";

            if (data.length === 0) {
                container.innerHTML = "<p>No ingredients found.</p>";
                return;
            }

            data.forEach(ingredient => {
                const line = document.createElement("p");
                line.textContent = `${ingredient.quantity} ${ingredient.unit} of ${ingredient.name}`;
                container.appendChild(line);
            });
        })
        .catch(error => {
            console.error("Erreur :", error);
            alert("❌ Error generating shopping list");
        });
    });
    }
    const exportBtn = document.getElementById("export-pdf-btn");

    if (exportBtn) {
        exportBtn.addEventListener("click", () => {
            const { jsPDF } = window.jspdf;
            const doc = new jsPDF({
                orientation: "portrait",
                unit: "mm",
                format: [80, 297], // format ticket de caisse
            });
    
            // 🖼️ Charge le logo (chemin relatif vers /static/img/logo.png par exemple)
            const logo = new Image();
            logo.src = "/images/logo.png"; // ✅ adapte selon ton chemin réel
    
            logo.onload = () => {
                // 🖼️ Affiche le logo en haut, centré
                doc.addImage(logo, "PNG", 20, 5, 40, 15); // x, y, largeur, hauteur
    
                const now = new Date();
                const date = now.toLocaleDateString();
                const time = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    
                // 🕓 Date & Heure
                doc.setFont("courier", "normal");
                doc.setFontSize(8);
                doc.text(`Date: ${date}`, 5, 25);
                doc.text(`Time: ${time}`, 5, 29);
    
                // 🛒 Titre
                doc.setFont("courier", "bold");
                doc.setFontSize(14);
                doc.text("My Shopping List", 40, 38, { align: "center" });
    
                // 🧾 Liste des ingrédients
                const lines = [];
                document.querySelectorAll("#shopping-list-content p").forEach((p) => {
                    lines.push(p.textContent);
                });
    
                let y = 46;
                doc.setFont("courier", "normal");
                doc.setFontSize(10);
    
                lines.forEach((line) => {
                    doc.text(`• ${line}`, 5, y);
                    y += 6;
                });
    
                // ✍️ Signature
                doc.setFont("courier", "bold");
                doc.setFontSize(10);
                doc.text("By MealMaster", 40, y + 10, { align: "center" });
    
                // 💾 Enregistre le PDF
                doc.save("shopping_list.pdf");
            };
        });
    }
    
}

        

    );
    
