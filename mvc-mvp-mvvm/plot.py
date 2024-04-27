import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

# Read data from CSV file
data = pd.read_csv('clean_data.csv')

# Define the categories and values columns
categories_columns = ['view_total', 'spin_total']
values_columns = ['time', 'memory']

for category_column in categories_columns:
    for value_column in values_columns:
        # Extract categories and values
        categories = data[category_column]
        values = data[value_column]

        # Group values by categories
        grouped_data = {category: [] for category in categories.unique()}
        for category, value in zip(categories, values):
            grouped_data[category].append(value)

        # Create a boxplot
        plt.figure(figsize=(10, 6))  # Adjust size as needed
        bp = plt.boxplot(grouped_data.values(), patch_artist=True, labels=grouped_data.keys(), showfliers=False, boxprops=dict(facecolor='white', color='black'), medianprops=dict(color='black'))

        # Add labels and title
        if category_column == 'view_total':
            plt.xlabel('Number of Views')
            plt.title(f'Boxplot of {value_column.capitalize()} by Number of Views')
        elif category_column == 'spin_total':
            plt.xlabel('Number of TextBox')
            plt.title(f'Boxplot of {value_column.capitalize()} by Number of TextBox')
        plt.ylabel(value_column.capitalize())

        # Add median, Q1, Q3 annotations
        for i, category in enumerate(grouped_data.keys()):
            median = np.median(grouped_data[category])
            q1 = np.percentile(grouped_data[category], 25)
            q3 = np.percentile(grouped_data[category], 75)
            # plt.text(i + 1, q1 - 0.01 * np.ptp(plt.ylim()), f'Q1={int(q1)}', ha='center', va='top')
            # plt.text(i + 1, median + 0.01 * np.ptp(plt.ylim()), f'Median={int(median)}', ha='left', va='top')
            # plt.text(i + 1, q3 + 0.01 * np.ptp(plt.ylim()), f'Q3={int(q3)}', ha='center', va='bottom')

        # Add average annotation with an offset
        for i, category in enumerate(grouped_data.keys()):
            avg = np.mean(grouped_data[category])
            plt.scatter(i + 1, avg, marker='x', color='black', zorder=5)  # Add 'X' symbol for average
            # plt.text(i + 1, avg - 0.01 * np.ptp(plt.ylim()), f'Avg={int(avg)}', ha='right', va='bottom')

        # Save plot to a PDF file
        plt.savefig(f'plot_{category_column}_{value_column}.pdf', bbox_inches='tight', pad_inches=0.2, dpi=300, format='pdf', transparent=True)
        plt.show()
